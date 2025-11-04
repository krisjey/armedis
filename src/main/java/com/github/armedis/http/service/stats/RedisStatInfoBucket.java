package com.github.armedis.http.service.stats;

import java.io.StringReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.redis.connection.pool.RedisConnectionPool;
import com.github.armedis.redis.info.RedisInfoAggregator;
import com.github.armedis.redis.info.RedisInfoVo;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;

/**
 * Redis cluster node status info command result --> redis status
 */
@Component
@Configuration
@EnableScheduling
public class RedisStatInfoBucket {
    /*
     * redis에 armedis:redis:stat:lock:unistimestamp(armedis-time) ttl 300초
     * 해당 키가 존재한다면 데이터 조회만 수행
     * 존재하지 않으면 수행 후 키 데이터 업데이트.
     * 데이터 구조 httl?
     * 
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private CircularFifoQueue<RedisStatsInfo> redisStatsInfoList = new CircularFifoQueue<>(10);

    private List<RedisClusterNodeInfo> redisNodeInfoList;

    @Autowired
    private ArmedisConfiguration armedisConfiguration;

    private ObjectMapper mapper = configMapper();

    @Autowired
    private RedisConnectionPool<String, String> redisConnectionPool;

    public String getStats() {
        String stats = null;
        try {
            stats = mapper.writeValueAsString(redisStatsInfoList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }

    /**
     * @return
     */
    private ObjectMapper configMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // This option increases the response data size.
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.setSerializationInclusion(Include.ALWAYS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        return mapper;
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(2); // 원하는 크기로 조절

        return scheduler;
    }

    @Scheduled(fixedRate = 1000) // 1000ms
    public void redisStatPolling() throws Throwable {
        // TODO Cluster랑 None Cluster 분리.
        if (!armedisConfiguration.isStatEnabled()) {
            return;
        }

        /**
         * TODO RedisConnector사용? 아니면 Cluster connection 계속 유지?
         * 클러스터 노드 변경되었을 때 필요.
         * 0. 노드 접속 정보 추출 1. polling 문자열 Return per sec. 2. Convert to RedisStatsInfo
         * per sec, every node RedisInfoVo 3.
         */
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.systemDefault());

        String clusterNodes = getClusterNodesCommandResult(redisConnectionPool);
        redisNodeInfoList = convertNodeInfoList(clusterNodes);

        RedisStatsInfo redisStatsInfo = new RedisStatsInfo(currentTime);

        String info = null;
        String redisNodeIp = null;

        // statsInfo
        for (RedisClusterNodeInfo redisNodeInfo : redisNodeInfoList) {
            try {
                StatefulRedisClusterConnection<String, String> connection = redisConnectionPool.getClusterConnection();
                StatefulRedisConnection<String, String> nodeConnection = connection.getConnection(redisNodeInfo.id());

                redisNodeIp = redisNodeInfo.ip();
                // send info command
                info = nodeConnection.sync().info();
                redisConnectionPool.returnObject(connection);

                // update stat info
                RedisInfoVo redisInfo = RedisInfoVo.from(info, armedisConfiguration.isAddContentSection());

                redisInfo.getServer().setHost(redisNodeIp);
                redisInfo.getServer().setTcpPort(redisNodeInfo.listenPort());

                printStatPollingLog(redisStatsInfo, redisNodeInfo, redisInfo);

                String redisInfoId = redisInfo.getServer().getHost() + ":" + redisInfo.getServer().getTcpPort();
                redisStatsInfo.put(redisInfoId, redisInfo);
                // 현재 시간기준(초단위)
            }
            catch (Exception e) {
                logger.error("Error when parsing info command! ", e);
            }
        }

        // Calculate sum using Collector pattern
        RedisInfoVo sumRedisInfoVo = RedisInfoAggregator.aggregate(
            redisStatsInfo.getRedisInfoList().values()
        );
        
        // Set host information from last processed node
        if (redisNodeIp != null) {
            sumRedisInfoVo.getServer().setHost(redisNodeIp);
        }

        redisStatsInfo.put("sum", sumRedisInfoVo);
        logger.debug("TOTAL OPS " + sumRedisInfoVo.getStats().getInstantaneousOpsPerSec());
        
        if (redisStatsInfoList.isAtFullCapacity()) {
            redisStatsInfoList.remove();
        }

        redisStatsInfoList.add(redisStatsInfo);
    }

    /**
     * 
     * @param redisStatsInfo
     * @param redisNodeInfo
     * @param redisInfo
     */
    private void printStatPollingLog(RedisStatsInfo redisStatsInfo, RedisClusterNodeInfo redisNodeInfo, RedisInfoVo redisInfo) {
        if (armedisConfiguration.isLoggingEnabled()) {
            logger.info("{}:{} {} {}", redisInfo.getServer().getHost(), redisInfo.getServer().getTcpPort(), redisNodeInfo.id(), redisInfo.toJsonString());
        }
    }

    private String getClusterNodesCommandResult(RedisConnectionPool<String, String> redisConnectionPool) {
        String nodesInfo = null;
        try {
            StatefulRedisClusterConnection<String, String> connection = redisConnectionPool.getClusterConnection();
            nodesInfo = connection.sync().clusterNodes();
            redisConnectionPool.returnObject(connection);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return nodesInfo;
    }

    private List<RedisClusterNodeInfo> convertNodeInfoList(String clusterNodes) {
        List<RedisClusterNodeInfo> redisNodeInfo = new ArrayList<RedisClusterNodeInfo>();
        List<String> nodeInfoStrings = IOUtils.readLines(new StringReader(clusterNodes));

        for (String nodeInfoString : nodeInfoStrings) {
            RedisClusterNodeInfo nodeInfo = RedisClusterNodeInfoConverter.convert(nodeInfoString);

            redisNodeInfo.add(nodeInfo);
        }

        return redisNodeInfo;
    }
}