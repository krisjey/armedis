package com.github.armedis.http.service.stats;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.queue.CircularFifoQueue;
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
import com.github.armedis.config.RedisMultiNodeCommander;
import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.command.management.RedisCommandStatsParser;
import com.github.armedis.redis.command.management.vo.CommandStatsVO;
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

    private CircularFifoQueue<RedisStatsInfo> redisStatsList = new CircularFifoQueue<>(10);

    private CommandStatsVO lastCommandStats;

    private ArmedisConfiguration armedisConfiguration;

    private final RedisServerDetector redisServerDetector;

    private ObjectMapper mapper = configMapper();

    private RedisMultiNodeCommander redisMultiNodeCommander;

    public RedisStatInfoBucket(ArmedisConfiguration armedisConfiguration, RedisMultiNodeCommander redisMultiNodeCommander, RedisServerDetector redisServerDetector) {
        this.armedisConfiguration = armedisConfiguration;
        this.redisMultiNodeCommander = redisMultiNodeCommander;
        this.redisServerDetector = redisServerDetector;
    }

    public String getStats() {
        String stats = null;
        try {
            stats = mapper.writeValueAsString(redisStatsList);
        }
        catch (JsonProcessingException e) {
            logger.error("Can not parse json from stats info list", e);
        }

        return stats;
    }

    public String getCommandStats() {
        String stats = null;
        try {
            stats = mapper.writeValueAsString(this.lastCommandStats);
        }
        catch (JsonProcessingException e) {
            logger.error("Can not parse json from stats info list", e);
        }
// TODO CRLF 제거 필요.
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

        scheduler.setPoolSize(3); // 원하는 크기로 조절

        return scheduler;
    }

    @Scheduled(fixedRate = 1000) // start every 1000ms term, except running time is over the 1000ms then starting no delay.
    public void redisStatPolling() throws Throwable {
        if (!armedisConfiguration.isStatEnabled()) {
            return;
        }

        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.systemDefault());

        RedisStatsInfo redisStatsInfo = new RedisStatsInfo(currentTime);

        String redisNodeIp = null;

        Map<String, String> commandStatsResultMap = new HashMap<String, String>();

        // statsInfo
        for (RedisNode redisNode : redisServerDetector.getAllNodes()) {
            try {
                String info = this.redisMultiNodeCommander.getNodeInfo(redisNode);

                // update stat info
                RedisInfoVo redisInfo = RedisInfoVo.from(info, armedisConfiguration.isAddContentSection());
                redisNodeIp = redisNode.getHost();
                redisInfo.getServer().setHost(redisNodeIp);
                redisInfo.getServer().setTcpPort(redisNode.getPort());

                printStatPollingLog(redisStatsInfo, redisNode, redisInfo);

                String redisInfoId = redisInfo.getServer().getHost() + ":" + redisInfo.getServer().getTcpPort();
                redisStatsInfo.put(redisInfoId, redisInfo);

                // info commandstats call by nodes.
                commandStatsResultMap.put(redisNode.toKey(), this.redisMultiNodeCommander.getCommandStats(redisNode));
            }
            catch (Exception e) {
                logger.error("Error when parsing info command! ", e);
            }
        }

        // Calculate sum using Collector pattern
        RedisInfoVo sumRedisInfoVo = RedisInfoAggregator.aggregate(redisStatsInfo.getRedisInfoMap().values());

        // Set host information from last processed node
        if (redisNodeIp != null) {
            sumRedisInfoVo.getServer().setHost(redisNodeIp);
        }

        redisStatsInfo.put("sum", sumRedisInfoVo);
        logger.debug("TOTAL OPS " + sumRedisInfoVo.getStats().getInstantaneousOpsPerSec());

        if (redisStatsList.isAtFullCapacity()) {
            redisStatsList.remove();
        }

        redisStatsList.add(redisStatsInfo);

        RedisCommandStatsParser parser = new RedisCommandStatsParser();

        List<CommandStatsVO> list = commandStatsResultMap.values().stream()
                .map(parser::parseCommandStats)
                .collect(Collectors.toList());

        // sum commandstats
        // update static data for commandstats
        this.lastCommandStats = CommandStatsAggregator.aggregate(list);
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

    public Map<String, RedisInfoVo> getFirstRedisInfoMap() {
        return this.redisStatsList.element().getRedisInfoMap();
    }
}