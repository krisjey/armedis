package com.github.armedis.redis.command.management;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.connection.RedisServerDetector;

// TODO 사용하지 않음 삭제 필요.
public class RedisConfigService {

    private final RedisServerDetector detector;
    private final LettuceRedisNodeCommandExecutor executor;

    public RedisConfigService(RedisServerDetector detector,
            LettuceRedisNodeCommandExecutor executor) {
        this.detector = detector;
        this.executor = executor;
    }

    /**
     * 모든 데이터 노드(master + replica)에 대해 CONFIG GET pattern 수행.
     */
    public Map<RedisNode, Map<String, String>> configGetAllNodes(String pattern) {
        Set<RedisNode> allNodes = detector.getAllNodes();
        return doConfigGet(allNodes, pattern);
    }

    /**
     * Master 노드에만 CONFIG GET pattern 수행.
     */
    public Map<RedisNode, Map<String, String>> configGetMasters(String pattern) {
        Set<RedisNode> masterNodes = detector.getMasterNodes();
        return doConfigGet(masterNodes, pattern);
    }

    /**
     * Replica 노드에만 CONFIG GET pattern 수행.
     */
    public Map<RedisNode, Map<String, String>> configGetReplicas(String pattern) {
        Set<RedisNode> replicaNodes = detector.getReplicaNodes();
        return doConfigGet(replicaNodes, pattern);
    }

    /**
     * Sentinel 노드에 대해 CONFIG GET pattern 수행 (필요 시).
     */
    public Map<RedisNode, Map<String, String>> configGetSentinels(String pattern) {
        Set<RedisNode> sentinelNodes = detector.getSentinelNodes();
        return doConfigGet(sentinelNodes, pattern);
    }

    /**
     * 모든 데이터 노드에 대해 CONFIG SET parameter value 수행.
     */
    public Map<RedisNode, String> configSetAllNodes(String parameter, String value) {
        Set<RedisNode> allNodes = detector.getAllNodes();
        return doConfigSet(allNodes, parameter, value);
    }

    /**
     * Master 노드에만 CONFIG SET parameter value 수행.
     */
    public Map<RedisNode, String> configSetMasters(String parameter, String value) {
        Set<RedisNode> masterNodes = detector.getMasterNodes();
        return doConfigSet(masterNodes, parameter, value);
    }

    /**
     * Replica 노드에만 CONFIG SET parameter value 수행.
     */
    public Map<RedisNode, String> configSetReplicas(String parameter, String value) {
        Set<RedisNode> replicaNodes = detector.getReplicaNodes();
        return doConfigSet(replicaNodes, parameter, value);
    }

    /**
     * Sentinel 노드에 CONFIG SET 수행(필요 시).
     */
    public Map<RedisNode, String> configSetSentinels(String parameter, String value) {
        Set<RedisNode> sentinelNodes = detector.getSentinelNodes();
        return doConfigSet(sentinelNodes, parameter, value);
    }

    // ========== 내부 구현 ==========

    private Map<RedisNode, Map<String, String>> doConfigGet(Collection<RedisNode> nodes, String key) {
        return executor.executeOnEach(nodes, commands -> {
            return commands.configGet(key);
        });
    }

    private Map<RedisNode, String> doConfigSet(Collection<RedisNode> nodes,
            String parameter, String value) {
        return executor.executeOnEach(nodes, commands -> commands.configSet(parameter, value));
    }

    private Map<String, String> toConfigMap(List<String> raw) {
        Map<String, String> map = new LinkedHashMap<>();
        if (raw == null) {
            return map;
        }
        for (int i = 0; i + 1 < raw.size(); i += 2) {
            String key = raw.get(i);
            String val = raw.get(i + 1);
            map.put(key, val);
        }
        return map;
    }
}