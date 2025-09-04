package com.github.armedis.http.service.stats;

import java.io.StringReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.github.armedis.redis.info.RedisInfoVo;
import com.github.armedis.redis.info.ReflectionManipulator;
import com.github.armedis.redis.info.StatsBaseVo;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;

/**
 * Redis cluster node status info command result --> redis status
 */
@Component
@Configuration
@EnableScheduling
public class RedisStatInfoBucket {
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

        // calculate sum.
        // Create dummy info object from last data. info object can not create
        // 기본값 생성.
//        RedisInfoVo sumRedisInfoVo = RedisInfoVo.from(info, armedisConfiguration.isAddContentSection());
//        sumRedisInfoVo.getServer().setHost(redisNodeIp);

        RedisInfoVo sumRedisInfoVo = null;
        int i = 0;
        for (Entry<String, RedisInfoVo> item : redisStatsInfo.getRedisInfoList().entrySet()) {
            i++;
            RedisInfoVo redisInfoVo = item.getValue();
            if (i == 1) { // first time
                sumRedisInfoVo = redisInfoVo;
                continue;
            }

            accumulateStatValue(sumRedisInfoVo, redisInfoVo);
        }

        redisStatsInfo.put("sum", sumRedisInfoVo);
        logger.debug("TOTAL OPS " + sumRedisInfoVo.getStats().getInstantaneousOpsPerSec());
        if (redisStatsInfoList.isAtFullCapacity()) {
            redisStatsInfoList.remove();
        }

        redisStatsInfoList.add(redisStatsInfo);
    }

    /**
     * Compare, Sum every Sub VO and then set value to sumVo
     * @param sumRedisInfoVo
     * @param redisInfoVo
     */
    private void accumulateStatValue(RedisInfoVo sumRedisInfoVo, RedisInfoVo redisInfoVo) {

        accumulateStatSubVo(sumRedisInfoVo.getServer(), redisInfoVo.getServer());
        accumulateStatSubVo(sumRedisInfoVo.getClients(), redisInfoVo.getClients());
        accumulateStatSubVo(sumRedisInfoVo.getMemory(), redisInfoVo.getMemory());
        accumulateStatSubVo(sumRedisInfoVo.getPersistence(), redisInfoVo.getPersistence());
        accumulateStatSubVo(sumRedisInfoVo.getStats(), redisInfoVo.getStats());
        accumulateStatSubVo(sumRedisInfoVo.getReplication(), redisInfoVo.getReplication());
        accumulateStatSubVo(sumRedisInfoVo.getCpu(), redisInfoVo.getCpu());
        accumulateStatSubVo(sumRedisInfoVo.getModules(), redisInfoVo.getModules());
        accumulateStatSubVo(sumRedisInfoVo.getErrorstats(), redisInfoVo.getErrorstats());
        accumulateStatSubVo(sumRedisInfoVo.getCluster(), redisInfoVo.getCluster());

//        System.out.println(sumRedisInfoVo.getStats().getInstantaneousInputKbps() + "-" + redisInfoVo.getStats().getInstantaneousInputKbps());
        // cluster이면 0만 사용.

//        keyList = redisInfoVo.getKeyspace().operationKeyList();
    }

    /**
     * @param sumRedisInfoVo 
     * @param baseVo 
     * 
     */
    private void accumulateStatSubVo(StatsBaseVo sumBaseVo, StatsBaseVo baseVo) {
        Map<String, String> keyList = baseVo.operationKeyList();

        for (Entry<String, String> item : keyList.entrySet()) {
            String fieldName = item.getKey();
            String operation = item.getValue();

//            String operation = null;
//            String dataType = null;
//            String[] temp = StringUtils.split(operationAndType, '-');
//            operation = temp[0];
//            if (temp.length == 2) {
//                dataType = temp[1];
//            }

            // 필드명으로 두 클래스의 값 가져와서 비교 후 할당.
            Object sumValue = ReflectionManipulator.getMethodInvokeResult(sumBaseVo, fieldName);
            Object targetValue = ReflectionManipulator.getMethodInvokeResult(baseVo, fieldName);

            if ((sumValue == null || targetValue == null) && !operation.equals(StatsBaseVo.EMPTY)) {
                logger.info("[{}] [{}] [{}]", fieldName, sumValue, targetValue);
            }

            switch (operation) {
                case StatsBaseVo.SUM:
                    String resultSumValue = calculateSumValue(sumValue, targetValue);
                    ReflectionManipulator.setFieldValue(sumBaseVo, fieldName, resultSumValue);
                    break;
                case StatsBaseVo.CONCAT:
                    ReflectionManipulator.setFieldValue(sumBaseVo, fieldName, sumValue + "," + targetValue);
                    break;
                case StatsBaseVo.DIFF:
                    String compareValue = diffValue(sumValue, targetValue, fieldName);
                    ReflectionManipulator.setFieldValue(sumBaseVo, fieldName, compareValue);
                    break;
                case StatsBaseVo.EMPTY:
                    ReflectionManipulator.setFieldValue(sumBaseVo, fieldName, "-");
                    break;
                case StatsBaseVo.MAX:
                    String maxValue = maxValue(sumValue, targetValue);
                    ReflectionManipulator.setFieldValue(sumBaseVo, fieldName, maxValue);
                    break;
                case StatsBaseVo.MIN:
                    String minValue = minValue(sumValue, targetValue);
                    ReflectionManipulator.setFieldValue(sumBaseVo, fieldName, minValue);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * @param sumValue
     * @param targetValue
     * @return
     */
    private String maxValue(Object sumValue, Object targetValue) {
        String resultSumValue = null;
        try {
            if (sumValue instanceof Number && sumValue.getClass().getSimpleName().equals(targetValue.getClass().getSimpleName())) {
                Number sumValueNumber = (Number) sumValue;
                Number targetValueNumber = (Number) targetValue;

                Double maxValue = Double.max(sumValueNumber.doubleValue(), targetValueNumber.doubleValue());
                resultSumValue = maxValue.toString();
            }
        }
        catch (Exception e) {
            logger.error("Cannot create class {}", sumValue.getClass().getSimpleName(), e);
        }

        return resultSumValue;
    }

    /**
     * @param sumValue
     * @param targetValue
     * @return
     */
    private String minValue(Object sumValue, Object targetValue) {
        String resultSumValue = null;
        try {
            if (sumValue instanceof Number && sumValue.getClass().getSimpleName().equals(targetValue.getClass().getSimpleName())) {
                Number sumValueNumber = (Number) sumValue;
                Number targetValueNumber = (Number) targetValue;

                Double minValue = Double.min(sumValueNumber.doubleValue(), targetValueNumber.doubleValue());
                resultSumValue = minValue.toString();
            }
        }
        catch (Exception e) {
            logger.error("Cannot create class {}", sumValue.getClass().getSimpleName(), e);
        }

        return resultSumValue;
    }

    /**
     * If the values are different, add (*) at the end if it is a string, and convert it to a negative number if it is a number.
     * diffString(*), -123
     * @param sumValue
     * @param targetValue
     * @param fieldName 
     * @return
     */
    private String diffValue(Object sumValue, Object targetValue, String fieldName) {
        String resultSumValue = null;
        try {
            if (sumValue instanceof Number && sumValue.getClass().getSimpleName().equals(targetValue.getClass().getSimpleName())) {
                Number sumValueNumber = (Number) sumValue;
                Number targetValueNumber = (Number) targetValue;

                resultSumValue = String.valueOf(targetValue);

                // double, float, int, long, and short
                switch (sumValue.getClass().getSimpleName()) {
                    case "Short", "Integer" -> {
                        if (sumValueNumber.intValue() != targetValueNumber.intValue()) {
                            resultSumValue = String.valueOf(-targetValueNumber.intValue());
                        }
                    }
                    case "Float" -> {
                        if (sumValueNumber.floatValue() != targetValueNumber.floatValue())
                            resultSumValue = String.valueOf(-targetValueNumber.floatValue());
                    }
                    case "Double" -> {
                        if (sumValueNumber.doubleValue() != targetValueNumber.doubleValue()) {
                            resultSumValue = String.valueOf(-targetValueNumber.doubleValue());
                        }
                    }
                    case "Long" -> {
                        if (sumValueNumber.longValue() != targetValueNumber.longValue()) {
                            resultSumValue = String.valueOf(-targetValueNumber.longValue());
                        }
                    }
                    default -> resultSumValue = String.format("%d, %d", sumValueNumber, targetValueNumber);
                }
            }
            else {
                if (sumValue.equals(targetValue)) {
                    resultSumValue = String.valueOf(targetValue);
                }
                else {
                    resultSumValue = String.valueOf(targetValue) + "(*)";
                }
            }
        }
        catch (Exception e) {
            logger.error("Cannot create field [{}], value[{}]", fieldName, sumValue, e);
        }

        return resultSumValue;
    }

    private String calculateSumValue(Object sumValue, Object targetValue) {
        String resultSumValue = null;
        try {
            if (sumValue instanceof Number && sumValue.getClass().getSimpleName().equals(targetValue.getClass().getSimpleName())) {
                Number sumValueNumber = (Number) sumValue;
                Number targetValueNumber = (Number) targetValue;

                // double, float, int, long, and short
                switch (sumValue.getClass().getSimpleName()) {
                    case "Short", "Integer" -> {
                        resultSumValue = String.valueOf(sumValueNumber.intValue() + targetValueNumber.intValue());
                    }
                    case "Float" -> {
                        resultSumValue = String.valueOf(sumValueNumber.floatValue() + targetValueNumber.floatValue());
                    }
                    case "Double" -> {
                        resultSumValue = String.valueOf(sumValueNumber.doubleValue() + targetValueNumber.doubleValue());
                    }
                    case "Long" -> {
                        resultSumValue = String.valueOf(sumValueNumber.longValue() + targetValueNumber.longValue());
                    }
                    // this is string.
                    default -> resultSumValue = String.format("%d, %d", sumValueNumber, targetValueNumber);
                }
            }
            else {
                logger.error("Can not sum {}, {}", sumValue, targetValue);
            }
        }
        catch (Exception e) {
            logger.error("Cannot create class {}", sumValue.getClass().getSimpleName(), e);
        }

        return resultSumValue;
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
        String nodes = null;
        try {
            StatefulRedisClusterConnection<String, String> connection = redisConnectionPool.getClusterConnection();
            nodes = connection.sync().clusterNodes();
            redisConnectionPool.returnObject(connection);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return nodes;
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
