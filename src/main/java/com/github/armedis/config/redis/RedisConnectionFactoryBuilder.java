package com.github.armedis.config.redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisNode;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;

/**
 * Redis 서버 타입에 따라 적절한 RedisConnectionFactory를 생성하는 빌더 클래스
 */
public class RedisConnectionFactoryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(RedisConnectionFactoryBuilder.class);

    private final RedisProperties properties;
    private final RedisInstanceType instanceType;
    private final Set<RedisNode> redisNodes;

    // Lettuce Pool 설정 (application.yml에서 주입받을 값들)
    private int maxActive = 8;
    private int maxIdle = 8;
    private int minIdle = 2;
    private Duration maxWait = Duration.ofMillis(2000);
    private Duration timeBetweenEvictionRuns = Duration.ofSeconds(60);

    public RedisConnectionFactoryBuilder(RedisProperties properties,
            RedisInstanceType instanceType,
            Set<RedisNode> redisNodes) {
        this.properties = properties;
        this.instanceType = instanceType;
        this.redisNodes = redisNodes;
    }

    /**
     * Lettuce Pool 설정 적용
     */
    public RedisConnectionFactoryBuilder withPoolConfig(int maxActive, int maxIdle, int minIdle,
            Duration maxWait, Duration timeBetweenEvictionRuns) {
        this.maxActive = maxActive;
        this.maxIdle = maxIdle;
        this.minIdle = minIdle;
        this.maxWait = maxWait;
        this.timeBetweenEvictionRuns = timeBetweenEvictionRuns;
        return this;
    }

    /**
     * Redis 서버 타입에 따라 적절한 ConnectionFactory 생성
     */
    public RedisConnectionFactory build() {
        logger.info("Building RedisConnectionFactory for type: {}", instanceType);

        switch (instanceType) {
            case STANDALONE:
                return buildStandaloneConnectionFactory();

            case REPLICA:
                return buildReplicationConnectionFactory();

            case SENTINEL:
                return buildSentinelConnectionFactory();

            case CLUSTER:
                return buildClusterConnectionFactory();

            default:
                throw new IllegalStateException("Unsupported Redis instance type: " + instanceType);
        }
    }

    /**
     * Standalone 모드 ConnectionFactory 생성
     */
    private RedisConnectionFactory buildStandaloneConnectionFactory() {
        logger.info("Creating Standalone Redis ConnectionFactory");

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(properties.getHost());
        config.setPort(properties.getPort());

        if (properties.hasPassword()) {
            config.setPassword(RedisPassword.of(properties.getPassword()));
        }

        LettuceConnectionFactory factory = new LettuceConnectionFactory(
                config,
                buildLettuceClientConfiguration());
        factory.afterPropertiesSet();

        return factory;
    }

    /**
     * Replication (Master-Slave) 모드 ConnectionFactory 생성
     */
    private RedisConnectionFactory buildReplicationConnectionFactory() {
        logger.info("Creating Replication Redis ConnectionFactory with {} nodes", redisNodes.size());

        // Master 노드 찾기
        RedisNode masterNode = redisNodes.stream()
                .filter(node -> node.getRedisNodeType() == com.github.armedis.redis.RedisNodeType.MASTER)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No master node found in replication setup"));

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(masterNode.getHost());
        config.setPort(masterNode.getPort());

        if (properties.hasPassword()) {
            config.setPassword(RedisPassword.of(properties.getPassword()));
        }

        // Read-from-replica 설정을 위한 Client Configuration
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(buildPoolConfig())
                .readFrom(ReadFrom.REPLICA_PREFERRED) // Replica 우선 읽기
                .build();

        LettuceConnectionFactory factory = new LettuceConnectionFactory(config, clientConfig);
        factory.afterPropertiesSet();

        return factory;
    }

    /**
     * Sentinel 모드 ConnectionFactory 생성
     */
    private RedisConnectionFactory buildSentinelConnectionFactory() {
        logger.info("Creating Sentinel Redis ConnectionFactory");

        // Sentinel 노드 정보 수집
        Set<String> sentinelNodes = redisNodes.stream()
                .map(node -> node.getHost() + ":" + node.getPort())
                .collect(Collectors.toSet());

        if (sentinelNodes.isEmpty()) {
            throw new IllegalStateException("No sentinel nodes found");
        }

        // Master name은 첫 번째 Sentinel에 접속하여 조회
        String masterName = detectSentinelMasterName();

        logger.info("Detected Sentinel master name: {}", masterName);

        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(masterName);

        // Sentinel 노드들 추가
        for (RedisNode node : redisNodes) {
            sentinelConfig.sentinel(node.getHost(), node.getPort());
        }

        if (properties.hasPassword()) {
            sentinelConfig.setPassword(RedisPassword.of(properties.getPassword()));
        }

        LettuceConnectionFactory factory = new LettuceConnectionFactory(
                sentinelConfig,
                buildLettuceClientConfiguration());
        factory.afterPropertiesSet();

        return factory;
    }

    /**
     * Cluster 모드 ConnectionFactory 생성
     */
    private RedisConnectionFactory buildClusterConnectionFactory() {
        logger.info("Creating Cluster Redis ConnectionFactory with {} nodes", redisNodes.size());

        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();

        // 클러스터 노드 추가
        List<RedisNode> clusterNodes = new ArrayList<>(redisNodes);
        for (RedisNode node : clusterNodes) {
            clusterConfig.addClusterNode(
                    new org.springframework.data.redis.connection.RedisNode(
                            node.getHost(),
                            node.getPort()));
        }

        if (properties.hasPassword()) {
            clusterConfig.setPassword(RedisPassword.of(properties.getPassword()));
        }

        // Cluster 전용 Client Configuration
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(Duration.ofSeconds(30))
                .enableAllAdaptiveRefreshTriggers()
                .build();

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(buildPoolConfig())
                .clientOptions(ClusterClientOptions.builder()
                        .topologyRefreshOptions(topologyRefreshOptions)
                        .autoReconnect(true)
                        .build())
                .build();

        LettuceConnectionFactory factory = new LettuceConnectionFactory(clusterConfig, clientConfig);
        factory.afterPropertiesSet();

        return factory;
    }

    /**
     * Lettuce Client Configuration 빌드
     */
    private LettuceClientConfiguration buildLettuceClientConfiguration() {
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(buildPoolConfig())
                .build();
    }

    /**
     * Connection Pool 설정 생성
     */
    private GenericObjectPoolConfig buildPoolConfig() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWait(maxWait);
        poolConfig.setTimeBetweenEvictionRuns(timeBetweenEvictionRuns);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        return poolConfig;
    }

    /**
     * Sentinel Master Name 자동 감지
     * INFO Sentinel 명령을 사용하여 첫 번째 master 이름 반환
     */
    private String detectSentinelMasterName() {
        // 첫 번째 Sentinel 노드에 연결
        RedisNode sentinelNode = redisNodes.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No sentinel node available"));

        try {
            RedisURI redisURI = RedisURI.Builder
                    .redis(sentinelNode.getHost(), sentinelNode.getPort())
                    .build();

            io.lettuce.core.RedisClient client = io.lettuce.core.RedisClient.create(redisURI);
            io.lettuce.core.api.StatefulRedisConnection<String, String> connection = client.connect();

            // INFO Sentinel 명령 실행
            String info = connection.sync().info("Sentinel");
            connection.close();
            client.shutdown();

            // master0 파싱 (예: master0:name=mymaster,status=ok,...)
            String[] lines = info.split("\r\n");
            for (String line : lines) {
                if (line.startsWith("master0:")) {
                    String[] parts = line.split(",");
                    for (String part : parts) {
                        if (part.startsWith("name=")) {
                            return part.substring(5);
                        }
                    }
                }
            }

            throw new IllegalStateException("Could not detect sentinel master name from INFO Sentinel");

        }
        catch (Exception e) {
            logger.error("Failed to detect sentinel master name", e);
            throw new IllegalStateException("Failed to connect to sentinel and detect master name", e);
        }
    }
}