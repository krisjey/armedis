package com.github.armedis.http.service.stats;

import java.io.StringReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.config.RedisConfigManager;
import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.connection.RedisServerDetector;
import com.github.armedis.redis.info.RedisInfoAggregator;
import com.github.armedis.redis.info.RedisInfoVo;

/**
 * Redis cluster node status info command result --> redis status
 */
@Component
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

    private Set<RedisNode> redisNodeInfoList;

    private ArmedisConfiguration armedisConfiguration;

    private final RedisServerDetector redisServerDetector;

    private ObjectMapper mapper = configMapper();

    private RedisConfigManager redisConfigManager;

    public RedisStatInfoBucket(ArmedisConfiguration armedisConfiguration, RedisConfigManager redisConfigManager, RedisServerDetector redisServerDetector) {
        this.armedisConfiguration = armedisConfiguration;
        this.redisConfigManager = redisConfigManager;
        this.redisServerDetector = redisServerDetector;
    }

    public String getStats() {
        String stats = null;
        try {
            stats = mapper.writeValueAsString(redisStatsInfoList);
        }
        catch (JsonProcessingException e) {
            logger.error("Can not parse json from stats info list", e);
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
        if (!armedisConfiguration.isStatEnabled()) {
            return;
        }

        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.systemDefault());

        // TODO Cluster랑 None Cluster 분리.
        redisNodeInfoList = redisServerDetector.getAllNodes();

        RedisStatsInfo redisStatsInfo = new RedisStatsInfo(currentTime);

        String info = null;
        String redisNodeIp = null;

        // statsInfo
        for (RedisNode redisNodeInfo : redisNodeInfoList) {
            try {
                info = this.redisConfigManager.getNodeInfo(redisNodeInfo);

                // update stat info
                RedisInfoVo redisInfo = RedisInfoVo.from(info, armedisConfiguration.isAddContentSection());
                redisNodeIp = redisNodeInfo.getHost();
                redisInfo.getServer().setHost(redisNodeIp);
                redisInfo.getServer().setTcpPort(redisNodeInfo.getPort());

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
        RedisInfoVo sumRedisInfoVo = RedisInfoAggregator.aggregate(redisStatsInfo.getRedisInfoList().values());

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
    private void printStatPollingLog(RedisStatsInfo redisStatsInfo, RedisNode redisNodeInfo, RedisInfoVo redisInfo) {
        if (armedisConfiguration.isLoggingEnabled()) {
            logger.info("{}:{} {} {}", redisInfo.getServer().getHost(), redisInfo.getServer().getTcpPort(), redisNodeInfo.getHost(), redisInfo.toJsonString());
        }
    }

    private List<RedisClusterNodeInfo> convertNodeInfoList(String clusterNodes) {
        List<RedisClusterNodeInfo> redisNodeInfo = new ArrayList<RedisClusterNodeInfo>();
        List<String> nodeInfoStrings = IOUtils.readLines(new StringReader(clusterNodes));

        for (String nodeInfoString : nodeInfoStrings) {
            redisNodeInfo.add(RedisClusterNodeInfo.of(nodeInfoString));
        }

        return redisNodeInfo;
    }

    public CircularFifoQueue<RedisStatsInfo> getRedisStatsInfoList() {
        return this.redisStatsInfoList;
    }
}