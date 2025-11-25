package com.github.armedis.redis.connection;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.armedis.redis.RedisInstanceType;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.ArrayOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;

/**
 * Redis 서버의 구성 정보를 탐지하는 싱글톤 클래스.
 * seed 노드 정보를 받아 Redis 동작 방식(Cluster, Sentinel, Replica, Single)과
 * 구성 노드 정보를 조회한다.
 */
public class RedisServerDetector {

    private static final Duration CONNECTION_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration COMMAND_TIMEOUT = Duration.ofSeconds(3);

    private final String seedHost;
    private final Integer seedPort;

    private Set<RedisNode> allNodes;
    private Set<RedisNode> masterNodes;
    private Set<RedisNode> replicaNodes;
    private RedisInstanceType instanceType;

    /**
     * Redis 서버의 정보를 받아 해당 서버의 동작 방식과 구성 노드 정보를 찾는 클래스
     *
     * @param seedHost Redis 서버 호스트 (예: 192.168.56.105)
     * @param seedPort Redis 서버 포트 (예: 17001)
     */
    public RedisServerDetector(String seedHost, Integer seedPort) {
        this.seedHost = Objects.requireNonNull(seedHost, "seedHost must not be null");
        this.seedPort = Objects.requireNonNull(seedPort, "seedPort must not be null");
        this.allNodes = new LinkedHashSet<>();
        this.masterNodes = new LinkedHashSet<>();
        this.replicaNodes = new LinkedHashSet<>();
    }

    /**
     * seed 정보를 사용하여 Redis 서버의 동작 방식을 탐지하고 노드 정보를 수집한다.
     *
     * @return 탐지된 모든 Redis 노드 Set
     * @throws RedisDetectionException 연결 실패 또는 탐지 실패 시
     */
    public Set<RedisNode> detectRedisServerNodes() {
        try {
            Map<String, String> info = executeInfoCommand(seedHost, seedPort);

            if (isClusterMode(info)) {
                this.instanceType = RedisInstanceType.CLUSTER;
                detectClusterNodes();
            }
            else if (isSentinelMode(info)) {
                this.instanceType = RedisInstanceType.SENTINEL;
                detectSentinelNodes();
            }
            else if (hasReplicas(info)) {
                this.instanceType = RedisInstanceType.REPLICA;
                detectReplicaNodes(seedHost, seedPort, info);
            }
            else {
                this.instanceType = RedisInstanceType.STANDALONE;
                detectSingleNode(info);
            }

            return getAllNodes();
        }
        catch (RedisDetectionException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RedisDetectionException("Failed to detect Redis server nodes", e);
        }
    }

    /**
     * Redis 인스턴스 타입을 반환한다.
     */
    public RedisInstanceType getRedisInstanceType() {
        return instanceType;
    }

    /**
     * Redis 인스턴스 타입을 문자열로 반환한다.
     */
    public String getRedisInstanceTypeAsString() {
        return instanceType != null ? instanceType.name() : null;
    }

    /**
     * Redis 서버를 구성하고 있는 모든 노드 목록
     */
    public Set<RedisNode> getAllNodes() {
        return Collections.unmodifiableSet(allNodes);
    }

    /**
     * Master 역할을 가진 노드 목록
     */
    public Set<RedisNode> getMasterNodes() {
        return Collections.unmodifiableSet(masterNodes);
    }

    /**
     * Replica(Slave) 역할을 가진 노드 목록
     */
    public Set<RedisNode> getReplicaNodes() {
        return Collections.unmodifiableSet(replicaNodes);
    }

    private Map<String, String> executeInfoCommand(String host, int port) {
        RedisClient client = null;
        StatefulRedisConnection<String, String> connection = null;

        try {
            RedisURI uri = RedisURI.builder()
                    .withHost(host)
                    .withPort(port)
                    .withTimeout(CONNECTION_TIMEOUT)
                    .build();

            client = RedisClient.create(uri);
            connection = client.connect();
            connection.setTimeout(COMMAND_TIMEOUT);

            RedisCommands<String, String> commands = connection.sync();
            String infoResult = commands.info();

            return parseInfoResult(infoResult);

        }
        catch (Exception e) {
            throw new RedisDetectionException(
                    String.format("Failed to connect to Redis at %s:%d", host, port), e);
        }
        finally {
            closeQuietly(connection);
            shutdownQuietly(client);
        }
    }

    private Map<String, String> executeInfoSection(String host, int port, String section) {
        RedisClient client = null;
        StatefulRedisConnection<String, String> connection = null;

        try {
            RedisURI uri = RedisURI.builder()
                    .withHost(host)
                    .withPort(port)
                    .withTimeout(CONNECTION_TIMEOUT)
                    .build();

            client = RedisClient.create(uri);
            connection = client.connect();
            connection.setTimeout(COMMAND_TIMEOUT);

            RedisCommands<String, String> commands = connection.sync();
            String infoResult = commands.info(section);

            return parseInfoResult(infoResult);

        }
        catch (Exception e) {
            throw new RedisDetectionException(
                    String.format("Failed to execute INFO %s at %s:%d", section, host, port), e);
        }
        finally {
            closeQuietly(connection);
            shutdownQuietly(client);
        }
    }

    private List<Map<String, String>> executeSentinelMasters(String host, int port) {
        RedisClient client = null;
        StatefulRedisConnection<String, String> connection = null;

        try {
            RedisURI uri = RedisURI.builder()
                    .withHost(host)
                    .withPort(port)
                    .withTimeout(CONNECTION_TIMEOUT)
                    .build();

            client = RedisClient.create(uri);
            connection = client.connect();
            connection.setTimeout(COMMAND_TIMEOUT);

            RedisCommands<String, String> commands = connection.sync();

            List<Object> rawResult = commands.dispatch(
                    CommandType.SENTINEL,
                    new ArrayOutput<>(StringCodec.UTF8),
                    new CommandArgs<>(StringCodec.UTF8)
                            .add("MASTERS"));

            return parseNestedArrayToMapList(rawResult);

        }
        catch (Exception e) {
            throw new RedisDetectionException(
                    String.format("Failed to execute SENTINEL MASTERS at %s:%d", host, port), e);
        }
        finally {
            closeQuietly(connection);
            shutdownQuietly(client);
        }
    }

    private List<Map<String, String>> executeSentinelReplicas(String host, int port, String masterName) {
        RedisClient client = null;
        StatefulRedisConnection<String, String> connection = null;

        try {
            RedisURI uri = RedisURI.builder()
                    .withHost(host)
                    .withPort(port)
                    .withTimeout(CONNECTION_TIMEOUT)
                    .build();

            client = RedisClient.create(uri);
            connection = client.connect();
            connection.setTimeout(COMMAND_TIMEOUT);

            RedisCommands<String, String> commands = connection.sync();

            List<Object> rawResult = commands.dispatch(
                    CommandType.SENTINEL,
                    new ArrayOutput<>(StringCodec.UTF8),
                    new CommandArgs<>(StringCodec.UTF8)
                            .add("REPLICAS")
                            .add(masterName));

            return parseNestedArrayToMapList(rawResult);

        }
        catch (Exception e) {
            // REPLICAS 명령이 실패하면 SLAVES 명령 시도 (이전 버전 호환)
            return executeSentinelSlaves(host, port, masterName);
        }
        finally {
            closeQuietly(connection);
            shutdownQuietly(client);
        }
    }

    private List<Map<String, String>> executeSentinelSlaves(String host, int port, String masterName) {
        RedisClient client = null;
        StatefulRedisConnection<String, String> connection = null;

        try {
            RedisURI uri = RedisURI.builder()
                    .withHost(host)
                    .withPort(port)
                    .withTimeout(CONNECTION_TIMEOUT)
                    .build();

            client = RedisClient.create(uri);
            connection = client.connect();
            connection.setTimeout(COMMAND_TIMEOUT);

            RedisCommands<String, String> commands = connection.sync();

            List<Object> rawResult = commands.dispatch(
                    CommandType.SENTINEL,
                    new ArrayOutput<>(StringCodec.UTF8),
                    new CommandArgs<>(StringCodec.UTF8)
                            .add("SLAVES")
                            .add(masterName));

            return parseNestedArrayToMapList(rawResult);

        }
        catch (Exception e) {
            throw new RedisDetectionException(
                    String.format("Failed to execute SENTINEL SLAVES at %s:%d for master %s",
                            host, port, masterName),
                    e);
        }
        finally {
            closeQuietly(connection);
            shutdownQuietly(client);
        }
    }

    private String executeClusterNodes(String host, int port) {
        RedisClient client = null;
        StatefulRedisConnection<String, String> connection = null;

        try {
            RedisURI uri = RedisURI.builder()
                    .withHost(host)
                    .withPort(port)
                    .withTimeout(CONNECTION_TIMEOUT)
                    .build();

            client = RedisClient.create(uri);
            connection = client.connect();
            connection.setTimeout(COMMAND_TIMEOUT);

            RedisCommands<String, String> commands = connection.sync();
            return commands.clusterNodes();

        }
        catch (Exception e) {
            throw new RedisDetectionException(
                    String.format("Failed to execute CLUSTER NODES at %s:%d", host, port), e);
        }
        finally {
            closeQuietly(connection);
            shutdownQuietly(client);
        }
    }

    private Map<String, String> parseInfoResult(String info) {
        Map<String, String> result = new HashMap<>();
        if (info == null || info.isEmpty()) {
            return result;
        }

        String[] lines = info.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            int colonIndex = line.indexOf(':');
            if (colonIndex > 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                result.put(key, value);
            }
        }
        return result;
    }

    private boolean isClusterMode(Map<String, String> info) {
        String clusterEnabled = info.get("cluster_enabled");
        return "1".equals(clusterEnabled);
    }

    private boolean isSentinelMode(Map<String, String> info) {
        String redisMode = info.get("redis_mode");
        return "sentinel".equalsIgnoreCase(redisMode);
    }

    private boolean hasReplicas(Map<String, String> info) {
        String role = info.get("role");
        if ("master".equalsIgnoreCase(role)) {
            String connectedSlaves = info.get("connected_slaves");
            return connectedSlaves != null && Integer.parseInt(connectedSlaves) > 0;
        }
        else if ("slave".equalsIgnoreCase(role)) {
            return true;
        }
        return false;
    }

    // ========== Cluster Mode Detection ==========

    private void detectClusterNodes() {
        String clusterNodesOutput = executeClusterNodes(seedHost, seedPort);
        parseClusterNodes(clusterNodesOutput);
    }

    private void parseClusterNodes(String clusterNodesOutput) {
        if (clusterNodesOutput == null || clusterNodesOutput.isEmpty()) {
            return;
        }

        String[] lines = clusterNodesOutput.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            if (parts.length < 3) {
                continue;
            }

            // parts[1] = ip:port@cport 또는 ip:port
            String addressPart = parts[1];
            String[] addressSplit = addressPart.split("@")[0].split(":");
            if (addressSplit.length < 2) {
                continue;
            }

            String host = addressSplit[0];
            int port;
            try {
                port = Integer.parseInt(addressSplit[1]);
            }
            catch (NumberFormatException e) {
                continue;
            }

            // parts[2] = flags (예: master, slave, myself,master 등)
            String flags = parts[2].toLowerCase();

            // fail 또는 noaddr 상태인 노드는 제외
            if (flags.contains("fail") || flags.contains("noaddr")) {
                continue;
            }

            RedisNodeRole role;
            if (flags.contains("master")) {
                role = RedisNodeRole.MASTER;
            }
            else if (flags.contains("slave") || flags.contains("replica")) {
                role = RedisNodeRole.REPLICA;
            }
            else {
                continue;
            }

            addNode(host, port, role);
        }
    }

    // ========== Sentinel Mode Detection ==========

    private void detectSentinelNodes() {
        // seed가 Sentinel 노드인지 Redis 노드인지 확인
        Map<String, String> info = executeInfoCommand(seedHost, seedPort);

        if (isSentinelMode(info)) {
            detectFromSentinel(seedHost, seedPort);
        }
        else {
            // seed가 Redis 노드인 경우, Sentinel 정보를 찾아야 함
            detectSentinelFromRedisNode(info);
        }
    }

    private void detectFromSentinel(String sentinelHost, int sentinelPort) {
        List<Map<String, String>> masters = executeSentinelMasters(sentinelHost, sentinelPort);

        for (Map<String, String> master : masters) {
            String masterName = master.get("name");
            String masterHost = master.get("ip");
            String masterPortStr = master.get("port");

            if (masterHost == null || masterPortStr == null) {
                continue;
            }

            int masterPort;
            try {
                masterPort = Integer.parseInt(masterPortStr);
            }
            catch (NumberFormatException e) {
                continue;
            }

            // Master 노드 추가
            addNode(masterHost, masterPort, RedisNodeRole.MASTER);

            // 해당 Master의 Replica 조회
            List<Map<String, String>> replicas = executeSentinelReplicas(
                    sentinelHost, sentinelPort, masterName);

            for (Map<String, String> replica : replicas) {
                String replicaHost = replica.get("ip");
                String replicaPortStr = replica.get("port");

                if (replicaHost == null || replicaPortStr == null) {
                    continue;
                }

                int replicaPort;
                try {
                    replicaPort = Integer.parseInt(replicaPortStr);
                }
                catch (NumberFormatException e) {
                    continue;
                }

                addNode(replicaHost, replicaPort, RedisNodeRole.REPLICA);
            }
        }
    }

    private void detectSentinelFromRedisNode(Map<String, String> info) {
        // Redis 노드의 INFO에서 Sentinel 정보를 찾을 수 없으므로
        // Replication 정보를 기반으로 노드 탐지
        detectReplicaNodes(seedHost, seedPort, info);
    }

    // ========== Replica Mode Detection ==========

    private void detectReplicaNodes(String host, int port, Map<String, String> info) {
        String role = info.get("role");

        if ("slave".equalsIgnoreCase(role)) {
            // 현재 노드가 slave인 경우, master 정보 조회
            String masterHost = info.get("master_host");
            String masterPortStr = info.get("master_port");

            if (masterHost != null && masterPortStr != null) {
                int masterPort = Integer.parseInt(masterPortStr);
                Map<String, String> masterInfo = executeInfoSection(masterHost, masterPort, "replication");
                detectReplicaNodes(masterHost, masterPort, masterInfo);
            }
        }
        else if ("master".equalsIgnoreCase(role)) {
            // Master 노드 추가
            addNode(host, port, RedisNodeRole.MASTER);

            // connected_slaves 수만큼 slaveN 정보 파싱
            String connectedSlavesStr = info.get("connected_slaves");
            int connectedSlaves = connectedSlavesStr != null ? Integer.parseInt(connectedSlavesStr) : 0;

            Pattern slavePattern = Pattern.compile("slave(\\d+)");
            Pattern infoPattern = Pattern.compile("ip=([^,]+),port=(\\d+)");

            for (Map.Entry<String, String> entry : info.entrySet()) {
                Matcher slaveMatcher = slavePattern.matcher(entry.getKey());
                if (slaveMatcher.matches()) {
                    Matcher infoMatcher = infoPattern.matcher(entry.getValue());
                    if (infoMatcher.find()) {
                        String replicaHost = infoMatcher.group(1);
                        int replicaPort = Integer.parseInt(infoMatcher.group(2));
                        addNode(replicaHost, replicaPort, RedisNodeRole.REPLICA);
                    }
                }
            }
        }
    }

    // ========== Single Mode Detection ==========

    private void detectSingleNode(Map<String, String> info) {
        String role = info.get("role");
        RedisNodeRole nodeRole = "master".equalsIgnoreCase(role)
                ? RedisNodeRole.MASTER
                : RedisNodeRole.REPLICA;
        addNode(seedHost, seedPort, nodeRole);
    }

    // ========== Helper Methods ==========

    private void addNode(String host, int port, RedisNodeRole role) {
        RedisNode node = new RedisNode(host, port, role);
        allNodes.add(node);

        if (role == RedisNodeRole.MASTER) {
            masterNodes.add(node);
        }
        else {
            replicaNodes.add(node);
        }
    }

    /**
     * SENTINEL 명령 결과를 Map 리스트로 변환한다.
     * 응답 형식: [[key1, val1, key2, val2, ...], [key1, val1, ...], ...]
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, String>> parseNestedArrayToMapList(List<Object> rawResult) {
        List<Map<String, String>> result = new ArrayList<>();

        if (rawResult == null) {
            return result;
        }

        for (Object item : rawResult) {
            if (item instanceof List) {
                List<Object> innerList = (List<Object>) item;
                Map<String, String> map = new HashMap<>();

                for (int i = 0; i < innerList.size() - 1; i += 2) {
                    String key = String.valueOf(innerList.get(i));
                    String value = String.valueOf(innerList.get(i + 1));
                    map.put(key, value);
                }

                result.add(map);
            }
        }

        return result;
    }

    private void closeQuietly(StatefulRedisConnection<?, ?> connection) {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (Exception ignored) {
            }
        }
    }

    private void shutdownQuietly(RedisClient client) {
        if (client != null) {
            try {
                client.shutdown();
            }
            catch (Exception ignored) {
            }
        }
    }

    /**
     * Redis 노드 역할
     */
    public enum RedisNodeRole {
        MASTER,
        REPLICA
    }

    /**
     * Redis 노드 정보를 담는 VO 클래스
     */
    public static class RedisNode {
        private final String host;
        private final int port;
        private final RedisNodeRole role;

        public RedisNode(String host, int port, RedisNodeRole role) {
            this.host = host;
            this.port = port;
            this.role = role;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public RedisNodeRole getRole() {
            return role;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            RedisNode redisNode = (RedisNode) o;
            return port == redisNode.port && Objects.equals(host, redisNode.host);
        }

        @Override
        public int hashCode() {
            return Objects.hash(host, port);
        }

        @Override
        public String toString() {
            return String.format("RedisNode{host='%s', port=%d, role=%s}", host, port, role);
        }
    }

    /**
     * Redis 탐지 실패 시 발생하는 예외
     */
    public static class RedisDetectionException extends RuntimeException {
        public RedisDetectionException(String message) {
            super(message);
        }

        public RedisDetectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}