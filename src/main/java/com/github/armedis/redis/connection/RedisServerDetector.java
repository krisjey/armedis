package com.github.armedis.redis.connection;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisNode;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.ArrayOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;

/**
 * Redis 서버의 구성 정보를 탐지하는 클래스.
 * seed 노드 정보를 받아 Redis 동작 방식(Cluster, Sentinel, Replica, Single)과
 * 구성 노드 정보를 조회한다.
 */
public class RedisServerDetector {
    private static final Duration CONNECTION_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration COMMAND_TIMEOUT = Duration.ofSeconds(3);

    private static final Pattern SLAVE_KEY_PATTERN = Pattern.compile("slave(\\d+)");
    private static final Pattern SLAVE_INFO_PATTERN = Pattern.compile("ip=([^,]+),port=(\\d+)");

    private final String seedHost;
    private final Integer seedPort;
    private final String password;

    private final Set<RedisNode> allNodes = new LinkedHashSet<>();
    private final Set<RedisNode> masterNodes = new LinkedHashSet<>();
    private final Set<RedisNode> replicaNodes = new LinkedHashSet<>();
    private final Set<RedisNode> sentinelNodes = new LinkedHashSet<>();
    private RedisInstanceType instanceType;

    public RedisServerDetector(String seedHost, Integer seedPort, String password) {
        this.seedHost = Objects.requireNonNull(seedHost, "seedHost must not be null");
        this.seedPort = Objects.requireNonNull(seedPort, "seedPort must not be null");
        this.password = password;
        detectRedisServerNodes();
    }

    // ==================== Public Getters ====================

    public RedisInstanceType getRedisInstanceType() {
        return instanceType;
    }

    public Set<RedisNode> getAllNodes() {
        return Collections.unmodifiableSet(allNodes);
    }

    public Set<RedisNode> getMasterNodes() {
        return Collections.unmodifiableSet(masterNodes);
    }

    public Set<RedisNode> getReplicaNodes() {
        return Collections.unmodifiableSet(replicaNodes);
    }

    public Set<RedisNode> getSentinelNodes() {
        return Collections.unmodifiableSet(sentinelNodes);
    }

    public String getSeedHost() {
        return seedHost;
    }

    public Integer getSeedPort() {
        return seedPort;
    }

    public String getSeedPassword() {
        return password;
    }

    public boolean hasPassword() {
        return password != null && !password.trim().isEmpty();
    }

    // ==================== Core Detection Logic ====================

    private void detectRedisServerNodes() {
        try {
            Map<String, String> info = executeInfo(seedHost, seedPort, null);

            if (isClusterMode(info)) {
                this.instanceType = RedisInstanceType.CLUSTER;
                detectClusterNodes();
            }
            else if (isSentinelMode(info)) {
                this.instanceType = RedisInstanceType.SENTINEL;
                detectSentinelNodes(info);
            }
            else if (hasReplicas(info)) {
                this.instanceType = RedisInstanceType.REPLICA;
                detectReplicaNodes(seedHost, seedPort, info);
            }
            else {
                this.instanceType = RedisInstanceType.STANDALONE;
                addNode(seedHost, seedPort, determineRole(info));
            }
        }
        catch (RedisDetectionException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RedisDetectionException("Failed to detect Redis server nodes", e);
        }
    }

    // ==================== Template Method for Connection ====================

    /**
     * Redis 연결을 생성하고 명령을 실행한 후 자원을 정리하는 템플릿 메서드
     */
    private <T> T executeWithConnection(String host, int port,
            Function<RedisCommands<String, String>, T> action) {
        RedisClient client = null;
        StatefulRedisConnection<String, String> connection = null;

        try {
            RedisURI uri = buildRedisUri(host, port);
            client = RedisClient.create(uri);
            connection = client.connect();
            connection.setTimeout(COMMAND_TIMEOUT);

            return action.apply(connection.sync());
        }
        catch (Exception e) {
            throw new RedisDetectionException(
                    String.format("Failed to execute command at %s:%d", host, port), e);
        }
        finally {
            closeQuietly(connection);
            shutdownQuietly(client);
        }
    }

    @SuppressWarnings("deprecation")
    private RedisURI buildRedisUri(String host, int port) {
        return RedisURI.builder()
                .withHost(host)
                .withPort(port)
                .withPassword(password)
                .withTimeout(CONNECTION_TIMEOUT)
                .build();
    }

    // ==================== Command Execution Methods ====================

    private Map<String, String> executeInfo(String host, int port, String section) {
        return executeWithConnection(host, port, new Function<RedisCommands<String, String>, Map<String, String>>() {
            @Override
            public Map<String, String> apply(RedisCommands<String, String> commands) {
                String result = (section == null) ? commands.info() : commands.info(section);
                return parseInfoResult(result);
            }
        });
    }

    private String executeClusterNodes() {
        return executeWithConnection(seedHost, seedPort, new Function<RedisCommands<String, String>, String>() {
            @Override
            public String apply(RedisCommands<String, String> commands) {
                return commands.clusterNodes();
            }
        });
    }

    /**
     * SENTINEL 명령 실행 통합 메서드
     */
    private List<Map<String, String>> executeSentinelCommand(String host, int port,
            String subCommand, String... args) {
        return executeWithConnection(host, port, new Function<RedisCommands<String, String>, List<Map<String, String>>>() {
            @Override
            public List<Map<String, String>> apply(RedisCommands<String, String> commands) {
                CommandArgs<String, String> cmdArgs = new CommandArgs<>(StringCodec.UTF8).add(subCommand);
                for (String arg : args) {
                    cmdArgs.add(arg);
                }

                List<Object> rawResult = commands.dispatch(
                        CommandType.SENTINEL,
                        new ArrayOutput<>(StringCodec.UTF8),
                        cmdArgs);

                return parseNestedArrayToMapList(rawResult);
            }
        });
    }

    private List<Map<String, String>> executeSentinelReplicas(String host, int port, String masterName) {
        try {
            return executeSentinelCommand(host, port, "REPLICAS", masterName);
        }
        catch (Exception e) {
            // REPLICAS 명령 실패 시 SLAVES 명령 시도 (이전 버전 호환)
            return executeSentinelCommand(host, port, "SLAVES", masterName);
        }
    }

    // ==================== Mode Detection Helpers ====================

    private boolean isClusterMode(Map<String, String> info) {
        return "1".equals(info.get("cluster_enabled"));
    }

    private boolean isSentinelMode(Map<String, String> info) {
        return "sentinel".equalsIgnoreCase(info.get("redis_mode"));
    }

    private boolean hasReplicas(Map<String, String> info) {
        String role = info.get("role");
        if ("master".equalsIgnoreCase(role)) {
            String connectedSlaves = info.get("connected_slaves");
            return connectedSlaves != null && Integer.parseInt(connectedSlaves) > 0;
        }
        return "slave".equalsIgnoreCase(role);
    }

    private RedisNodeRole determineRole(Map<String, String> info) {
        return "master".equalsIgnoreCase(info.get("role"))
                ? RedisNodeRole.MASTER
                : RedisNodeRole.REPLICA;
    }

    // ==================== Cluster Mode Detection ====================

    private void detectClusterNodes() {
        String output = executeClusterNodes();
        if (output == null || output.isEmpty()) {
            return;
        }

        String[] lines = output.split("\\r?\\n");
        for (String line : lines) {
            parseClusterNodeLine(line.trim());
        }
    }

    private void parseClusterNodeLine(String line) {
        if (line.isEmpty()) {
            return;
        }

        String[] parts = line.split("\\s+");
        if (parts.length < 3) {
            return;
        }

        Optional<HostPort> hostPortOpt = parseHostPort(parts[1].split("@")[0]);
        if (!hostPortOpt.isPresent()) {
            return;
        }

        HostPort hp = hostPortOpt.get();
        String flags = parts[2].toLowerCase();

        if (flags.contains("fail") || flags.contains("noaddr")) {
            return;
        }

        if (flags.contains("master")) {
            addNode(hp.host, hp.port, RedisNodeRole.MASTER);
        }
        else if (flags.contains("slave") || flags.contains("replica")) {
            addNode(hp.host, hp.port, RedisNodeRole.REPLICA);
        }
    }

    // ==================== Sentinel Mode Detection ====================

    private void detectSentinelNodes(Map<String, String> info) {
        if (isSentinelMode(info)) {
            detectFromSentinel(seedHost, seedPort);
        }
        else {
            detectReplicaNodes(seedHost, seedPort, info);
        }
    }

    private void detectFromSentinel(String sentinelHost, int sentinelPort) {
        addNode(sentinelHost, sentinelPort, RedisNodeRole.SENTINEL);

        List<Map<String, String>> masters = executeSentinelCommand(sentinelHost, sentinelPort, "MASTERS");

        for (Map<String, String> master : masters) {
            String masterName = master.get("name");
            Optional<HostPort> hostPortOpt = parseHostPort(master.get("ip"), master.get("port"));

            if (!hostPortOpt.isPresent()) {
                continue;
            }

            HostPort hp = hostPortOpt.get();
            addNode(hp.host, hp.port, RedisNodeRole.MASTER);
            collectSentinelReplicas(sentinelHost, sentinelPort, masterName);
            collectOtherSentinels(sentinelHost, sentinelPort, masterName);
        }
    }

    private void collectSentinelReplicas(String sentinelHost, int sentinelPort, String masterName) {
        List<Map<String, String>> replicas = executeSentinelReplicas(sentinelHost, sentinelPort, masterName);

        for (Map<String, String> replica : replicas) {
            Optional<HostPort> hostPortOpt = parseHostPort(replica.get("ip"), replica.get("port"));
            if (hostPortOpt.isPresent()) {
                HostPort hp = hostPortOpt.get();
                addNode(hp.host, hp.port, RedisNodeRole.REPLICA);
            }
        }
    }

    private void collectOtherSentinels(String sentinelHost, int sentinelPort, String masterName) {
        List<Map<String, String>> sentinels = executeSentinelCommand(sentinelHost, sentinelPort, "SENTINELS", masterName);

        for (Map<String, String> sentinel : sentinels) {
            Optional<HostPort> hostPortOpt = parseHostPort(sentinel.get("ip"), sentinel.get("port"));
            if (hostPortOpt.isPresent()) {
                HostPort hp = hostPortOpt.get();
                addNode(hp.host, hp.port, RedisNodeRole.SENTINEL);
            }
        }
    }

    // ==================== Replica Mode Detection ====================

    private void detectReplicaNodes(String host, int port, Map<String, String> info) {
        String role = info.get("role");

        if ("slave".equalsIgnoreCase(role)) {
            // slave인 경우 master 정보 조회 후 재귀 호출
            Optional<HostPort> hostPortOpt = parseHostPort(info.get("master_host"), info.get("master_port"));

            if (hostPortOpt.isPresent()) {
                HostPort hp = hostPortOpt.get();
                Map<String, String> masterInfo = executeInfo(hp.host, hp.port, "replication");
                detectReplicaNodes(hp.host, hp.port, masterInfo);
            }
        }
        else if ("master".equalsIgnoreCase(role)) {
            addNode(host, port, RedisNodeRole.MASTER);
            collectReplicasFromMasterInfo(info);
        }
    }

    private void collectReplicasFromMasterInfo(Map<String, String> info) {
        Set<Map.Entry<String, String>> entries = info.entrySet();

        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (SLAVE_KEY_PATTERN.matcher(key).matches()) {
                Matcher infoMatcher = SLAVE_INFO_PATTERN.matcher(value);
                if (infoMatcher.find()) {
                    String replicaHost = infoMatcher.group(1);
                    int replicaPort = Integer.parseInt(infoMatcher.group(2));
                    addNode(replicaHost, replicaPort, RedisNodeRole.REPLICA);
                }
            }
        }
    }

    // ==================== Utility Methods ====================

    private void addNode(String host, int port, RedisNodeRole role) {
        RedisNode node = new RedisNode(host, port, role);

        switch (role) {
            case MASTER:
                allNodes.add(node);
                masterNodes.add(node);
                break;
            case SENTINEL:
                sentinelNodes.add(node);
                break;
            default:
                allNodes.add(node);
                replicaNodes.add(node);
                break;
        }
    }

    private Optional<HostPort> parseHostPort(String address) {
        if (address == null || address.isEmpty()) {
            return Optional.empty();
        }
        String[] parts = address.split(":");
        if (parts.length < 2) {
            return Optional.empty();
        }
        try {
            return Optional.of(new HostPort(parts[0], Integer.parseInt(parts[1])));
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<HostPort> parseHostPort(String host, String portStr) {
        if (host == null || portStr == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new HostPort(host, Integer.parseInt(portStr)));
        }
        catch (NumberFormatException e) {
            return Optional.empty();
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
                result.put(line.substring(0, colonIndex).trim(),
                        line.substring(colonIndex + 1).trim());
            }
        }
        return result;
    }

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
                    map.put(String.valueOf(innerList.get(i)), String.valueOf(innerList.get(i + 1)));
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

    // ==================== Inner Classes ====================

    private static class HostPort {
        final String host;
        final int port;

        HostPort(String host, int port) {
            this.host = host;
            this.port = port;
        }
    }

    public static class RedisDetectionException extends RuntimeException {
        private static final long serialVersionUID = -3505162426849543055L;

        public RedisDetectionException(String message) {
            super(message);
        }

        public RedisDetectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}