
package com.github.armedis.redis.command.management;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RequestRedisCommandName;
import com.github.armedis.redis.connection.RedisServerDetector;
import com.linecorp.armeria.common.HttpMethod;

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

@Component
@Scope("prototype")
@RequestRedisCommandName(RedisCommandEnum.CONFIG)
public class RedisConfigCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisConfigCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisConfigCommandRunner.class);

    private RedisConfigRequest redisRequest;

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisServerDetector redisServerDetector;

//    private final LettuceRedisNodeCommandExecutor executor;

    public RedisConfigCommandRunner(RedisConfigRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
        this.redisTemplate = redisTemplate;
//        this.executor = executor;
    }

// 정상 동작 코드.
//    @Override
//    public RedisCommandExecuteResult executeAndGet() {
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        Map<Object, Object> result = new HashMap<>();
//
//        if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
//            Object tempResult = redisTemplate.execute((RedisConnection connection) -> {
//                return connection.serverCommands().getConfig(key).get(key);
//            });
//            result.put(key, String.valueOf(tempResult));
//        }
//        else {
//            Optional<String> value = this.redisRequest.getValue();
//
//            Object tempResult = redisTemplate.execute((RedisConnection connection) -> {
//                connection.serverCommands().setConfig(key, value.get());
//                return true;
//            });
//
//            result.put((Object) key, String.valueOf(tempResult));
//        }
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }

    private Map<Object, Object> broadcastConfigToAllNodes(String key, String value) {
        Map<Object, Object> result = new HashMap<>();
        Set<RedisNode> nodes = redisServerDetector.getAllNodes();

        redisTemplate.execute((RedisConnection connection) -> {
            Object nativeConnection = connection.getNativeConnection();

            if (nativeConnection instanceof StatefulRedisClusterConnection) {

                @SuppressWarnings({ "unchecked", "resource" })
                StatefulRedisClusterConnection<byte[], byte[]> clusterConnection = (StatefulRedisClusterConnection<byte[], byte[]>) nativeConnection;

                for (RedisNode node : nodes) {
                    String nodeId = node.getHost() + ":" + node.getPort();
                    try {
                        RedisClusterCommands<byte[], byte[]> commands = clusterConnection.getConnection(node.getHost(), node.getPort()).sync();

                        commands.configSet(key, value);

                        result.put(nodeId, "true");
                        logger.info("CONFIG SET {} = {} on {}", key, value, nodeId);
                    }
                    catch (Exception e) {
                        result.put(nodeId, "false: " + e.getMessage());
                        logger.error("Failed CONFIG SET on {}: {}", nodeId, e.getMessage());
                    }
                }
            }
            return null;
        });

        return result;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        // 업데이트 시 내부 값 업데이트
        // AllowedConfigCommands.get(key).setCurrentValueFromDB(updatedValue.get(key));
        // SET 명령수행시 롤백 처리 : 롤백을 위해 이전 값 저장
        // GET 명령 수행시 노드들의 값이 다르면 (+) 기호 표시.

        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        Map<Object, Object> result = new HashMap<>();
        RedisInstanceType instanceType = redisServerDetector.getRedisInstanceType();

        if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
            // GET은 단일 노드 조회로 충분
            Object tempResult = redisTemplate.execute((RedisConnection connection) -> {
                return connection.serverCommands().getConfig(key).get(key);
            });
            result.put(key, String.valueOf(tempResult));
        }
        else {
            Optional<String> value = this.redisRequest.getValue();

            if (instanceType == RedisInstanceType.CLUSTER) {
                // 클러스터: 모든 노드에 CONFIG SET 브로드캐스트
                result = broadcastConfigToAllNodes(key, value.get());
            }
            else {
                // Standalone/Sentinel: 단일 실행
                redisTemplate.execute((RedisConnection connection) -> {
                    connection.serverCommands().setConfig(key, value.get());
                    return null;
                });
                result.put(key, "true");
            }
        }

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

//    public RedisCommandExecuteResult executeAndGet() {
//        logger.info(redisRequest.toString());
//        String key = this.redisRequest.getKey();
//        Optional<String> value = this.redisRequest.getValue();
//
//        // TODO 변경 필요.
//        RedisInstanceType instanceType = redisServerDetector.getRedisInstanceType();
//
//        try {
//            if (RedisInstanceType.STANDALONE.equals(instanceType)) {
//                return executeForStandalone(key, value);
//            }
//            else {
//                return executeForCluster(key, value);
//            }
//        }
//        catch (Exception e) {
//            logger.error("Error executing command: " + this.redisRequest.toString(), e);
//            throw e;
//        }
//    }
//
//    private RedisCommandExecuteResult executeForStandalone(String key, Optional<String> value) {
//        Set<RedisNode> nodes = this.redisServerDetector.getAllNodes();
//        ((ClusterOperations<String, Object>) redisTemplate.opsForCluster())
//        .execute(node, (RedisConnection connection) -> {
//            Map<String, String> configMap = connection.serverCommands().getConfig(key);
//            return configMap.get(key); 
//        });
//        
//        
//        if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
//            Object tempResult = redisTemplate.execute((RedisConnection connection) -> {
//                return connection.serverCommands().getConfig(key).get(key);
//            });
//
//            Map<String, String> result = redisTemplate.execute((RedisConnection connection) -> {
//                List<String> configResult = connection.serverCommands().getConfig(key);
//                return convertListToMap(configResult);
//            });
//            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//        }
//        else {
//            // SET 명령: 롤백을 위해 이전 값 저장
//            Map<String, String> previousValue = redisTemplate.execute((RedisConnection connection) -> {
//                List<String> configResult = connection.serverCommands().getConfig(key);
//                return convertListToMap(configResult);
//            });
//
//            try {
//                String setResult = redisTemplate.execute((RedisConnection connection) -> {
//                    connection.serverCommands().setConfig(key, value.get());
//                    return "OK";
//                });
//
//                // update value for allowed command value.
//                Map<String, String> updatedValue = redisTemplate.execute((RedisConnection connection) -> {
//                    List<String> configResult = connection.serverCommands().getConfig(key);
//                    return convertListToMap(configResult);
//                });
//                AllowedConfigCommands.get(key).setCurrentValueFromDB(updatedValue.get(key));
//
//                return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(setResult);
//            }
//            catch (Exception e) {
//                logger.error("Error during config set for standalone, rolling back to previous value", e);
//                // 롤백 처리
//                if (previousValue != null && previousValue.containsKey(key)) {
//                    redisTemplate.execute((RedisConnection connection) -> {
//                        connection.serverCommands().setConfig(key, previousValue.get(key));
//                        return null;
//                    });
//                }
//                throw e;
//            }
//        }
//    }
//
//    private RedisCommandExecuteResult executeForCluster(String key, Optional<String> value) {
//        Set<RedisNode> nodes = RedisServerDetector.getAllNodes();
//
//        if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
//            return executeClusterGet(key, nodes);
//        }
//        else {
//            return executeClusterSet(key, value.get(), nodes);
//        }
//    }
//
//    private RedisCommandExecuteResult executeClusterGet(String key, Set<RedisNode> nodes) {
//        Map<RedisNode, String> nodeResults = new HashMap<>();
//        Exception lastException = null;
//
//        for (RedisNode node : nodes) {
//            try {
//                String nodeValue = redisTemplate.execute((RedisConnection connection) -> {
//                    List<String> configResult = connection.serverCommands().getConfig(key);
//                    Map<String, String> resultMap = convertListToMap(configResult);
//                    return resultMap.get(key);
//                });
//                nodeResults.put(node, nodeValue);
//            }
//            catch (Exception e) {
//                logger.error("Error executing config get on node: " + node, e);
//                lastException = e;
//            }
//        }
//
//        // 하나라도 실패하면 전체 실패
//        if (lastException != null) {
//            throw new RuntimeException("Failed to execute config get on one or more nodes", lastException);
//        }
//
//        // 노드별 값 비교 및 "(+)" 표시
//        String finalValue = checkNodeValuesAndAddMarker(nodeResults, key);
//        Map<String, String> result = new HashMap<>();
//        result.put(key, finalValue);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }
//
//    private RedisCommandExecuteResult executeClusterSet(String key, String newValue, Set<RedisNode> nodes) {
//        Map<RedisNode, String> previousValues = new HashMap<>();
//        Set<RedisNode> successNodes = new HashSet<>();
//        Exception lastException = null;
//
//        // 1단계: 모든 노드의 이전 값 저장
//        for (RedisNode node : nodes) {
//            try {
//                String prevValue = redisTemplate.execute((RedisConnection connection) -> {
//                    List<String> configResult = connection.serverCommands().getConfig(key);
//                    Map<String, String> resultMap = convertListToMap(configResult);
//                    return resultMap.get(key);
//                });
//                previousValues.put(node, prevValue);
//            }
//            catch (Exception e) {
//                logger.error("Error getting previous value from node: " + node, e);
//                lastException = e;
//            }
//        }
//
//        // 이전 값 조회 실패 시 전체 실패
//        if (lastException != null) {
//            throw new RuntimeException("Failed to get previous values from one or more nodes", lastException);
//        }
//
//        // 2단계: 모든 노드에 새 값 설정
//        for (RedisNode node : nodes) {
//            try {
//                redisTemplate.execute((RedisConnection connection) -> {
//                    connection.serverCommands().setConfig(key, newValue);
//                    return "OK";
//                });
//                successNodes.add(node);
//            }
//            catch (Exception e) {
//                logger.error("Error executing config set on node: " + node, e);
//                lastException = e;
//                break; // 실패 시 즉시 중단
//            }
//        }
//
//        // 3단계: 일부 노드 실패 시 롤백
//        if (lastException != null) {
//            logger.error("Config set failed on one or more nodes, rolling back all changes");
//
//            for (RedisNode successNode : successNodes) {
//                try {
//                    String previousValue = previousValues.get(successNode);
//                    if (previousValue != null) {
//                        redisTemplate.execute((RedisConnection connection) -> {
//                            connection.serverCommands().setConfig(key, previousValue);
//                            return null;
//                        });
//                        logger.info("Rolled back node: " + successNode);
//                    }
//                }
//                catch (Exception rollbackException) {
//                    logger.error("Error during rollback for node: " + successNode, rollbackException);
//                }
//            }
//
//            throw new RuntimeException("Failed to execute config set on one or more nodes, rolled back changes", lastException);
//        }
//
//        // 4단계: 성공 시 AllowedConfigCommands 업데이트 (첫 번째 노드 기준)
//        try {
//            Map<String, String> updatedValue = redisTemplate.execute((RedisConnection connection) -> {
//                List<String> configResult = connection.serverCommands().getConfig(key);
//                return convertListToMap(configResult);
//            });
//            AllowedConfigCommands.get(key).setCurrentValueFromDB(updatedValue.get(key));
//        }
//        catch (Exception e) {
//            logger.error("Error updating AllowedConfigCommands", e);
//        }
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult("OK");
//    }
//
//    private String checkNodeValuesAndAddMarker(Map<RedisNode, String> nodeResults, String key) {
//        if (nodeResults.isEmpty()) {
//            return null;
//        }
//
//        // 첫 번째 값을 기준으로 비교
//        String baseValue = nodeResults.values().iterator().next();
//        boolean hasDifferentValues = false;
//
//        for (String nodeValue : nodeResults.values()) {
//            if (!Objects.equals(baseValue, nodeValue)) {
//                hasDifferentValues = true;
//                break;
//            }
//        }
//
//        // 노드별로 다른 값이 있으면 "(+)" 추가
//        if (hasDifferentValues) {
//            return baseValue + "(+)";
//        }
//
//        return baseValue;
//    }
//
//    private Map<String, String> convertListToMap(List<String> configResult) {
//        Map<String, String> resultMap = new HashMap<>();
//        if (configResult != null && configResult.size() >= 2) {
//            for (int i = 0; i < configResult.size(); i += 2) {
//                resultMap.put(configResult.get(i), configResult.get(i + 1));
//            }
//        }
//        return resultMap;
//    }
//
//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//
//        if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
//            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(commands.configGet(key));
//        }
//        else {
//            Optional<String> value = this.redisRequest.getValue();
//            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(commands.configSet(key, value.get()));
//        }
//    }
//
//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        Optional<String> value = this.redisRequest.getValue();
//
//        // TODO command validator
//
//        Set<RedisNode> nodes = RedisServerDetector.getAllNodes();
//
//        Map<String, String> getResult = null;
//        String postResult = null;
//
//        // execute command to each nodes.
//        for (RedisNode node : nodes) {
//            try {
//                @SuppressWarnings("resource")
//                RedisConnector connector = new RedisConnector(node);
//                StatefulRedisConnection<String, String> connection = connector.connect();
//                if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
//                    getResult = connection.sync().configGet(key);
//                }
//                else {
//                    postResult = connection.sync().configSet(key, value.get());
//
//                    // update value for allowed command value.
//                    AllowedConfigCommands.get(key).setCurrentValueFromDB(connection.sync().configGet(key).get(key));
//                }
//            }
//            catch (Exception e) {
//                logger.error("Error command " + this.redisRequest.toString(), e);
//            }
//        }
//
//        if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
//            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(getResult);
//        }
//        else {
//            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(postResult);
//        }
//    }
}
