
package com.github.armedis.redis.connection.pool;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.RedisServerInfoMaker;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.support.ConnectionPoolSupport;

@Component
public class RedisConnectionPoolFactory implements RedisConnectionPool<String, String> {
    private RedisServerInfoMaker redisServerInfoMaker;

    // cluster connection pool
    private GenericObjectPool<StatefulRedisClusterConnection<String, String>> clusterConnectionPool;

    // single connection pool
    private GenericObjectPool<StatefulRedisConnection<String, String>> singleConnectionPool;

    @Autowired
    public RedisConnectionPoolFactory(RedisServerInfoMaker redisServerInfoMaker) {
        this.redisServerInfoMaker = redisServerInfoMaker;

        RedisInstanceType redisServerInfo = this.redisServerInfoMaker.getRedisServerInfo().getRedisInstanceType();
        switch (redisServerInfo) {
            case STANDALONE:
                this.singleConnectionPool = buildStandaloneConnectionPool();
                break;

            case SENTINEL:
                throw new NotImplementedException("Connection pool not implemented " + redisServerInfo.toString());

            case CLUSTER:
                this.clusterConnectionPool = buildClusterConnectionPool();
                break;

            case NOT_DETECTED:
                throw new NotImplementedException("Connection pool not implemented " + redisServerInfo.toString());

            default:
                throw new NotImplementedException("Connection pool not implemented " + redisServerInfo.toString());
        }
    }

    private GenericObjectPool<StatefulRedisConnection<String, String>> buildStandaloneConnectionPool() {
        requireNonNull(redisServerInfoMaker, "redis server info is null");
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(true)
                .refreshPeriod(Duration.ofSeconds(5))
                .enableAllAdaptiveRefreshTriggers()
                .build();

        Set<RedisNode> nodes = redisServerInfoMaker.getRedisServerInfo().getRedisNodes();

        // cluster node
        RedisURI clusterNode = null;
        for (RedisNode item : nodes) {
            clusterNode = RedisURI.create(item.getHost(), item.getPort());
        }

        RedisClient redisClient = RedisClient.create(clusterNode);
        redisClient.setOptions(ClusterClientOptions.builder()
                .topologyRefreshOptions(topologyRefreshOptions)
                .build());

        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxTotal(10);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setBlockWhenExhausted(true);

        GenericObjectPool<StatefulRedisConnection<String, String>> pool = ConnectionPoolSupport
                .createGenericObjectPool(redisClient::connect, poolConfig);

        return pool;
    }

    private GenericObjectPool<StatefulRedisClusterConnection<String, String>> buildClusterConnectionPool() {
        requireNonNull(redisServerInfoMaker, "redis server info is null");
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(true)
                .refreshPeriod(Duration.ofSeconds(5))
                .enableAllAdaptiveRefreshTriggers()
                .build();

        Set<RedisNode> nodes = redisServerInfoMaker.getRedisServerInfo().getRedisNodes();

        // cluster node
        RedisURI clusterNode = null;
        for (RedisNode item : nodes) {
            clusterNode = RedisURI.create(item.getHost(), item.getPort());
        }

        RedisClusterClient clusterClient = RedisClusterClient.create(clusterNode);
        clusterClient.setOptions(ClusterClientOptions.builder()
                .topologyRefreshOptions(topologyRefreshOptions)
                .build());

        GenericObjectPoolConfig<StatefulRedisClusterConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxTotal(10);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setBlockWhenExhausted(true);

        GenericObjectPool<StatefulRedisClusterConnection<String, String>> pool = ConnectionPoolSupport
                .createGenericObjectPool(clusterClient::connect, poolConfig);

        return pool;
    }

    @Override
    public StatefulRedisClusterConnection<String, String> getClusterConnection() throws Exception {
        StatefulRedisClusterConnection<String, String> connection = null;

        connection = clusterConnectionPool.borrowObject();

        return connection;
    }

    @Override
    public StatefulRedisConnection<String, String> getNonClusterConnection() throws Exception {
        StatefulRedisConnection<String, String> connection = null;

        connection = singleConnectionPool.borrowObject();

        return connection;
    }

    @Override
    public void returnObject(StatefulRedisClusterConnection<String, String> connection) throws Exception {
        clusterConnectionPool.returnObject(connection);
    }

    @Override
    public void returnObject(StatefulRedisConnection<String, String> connection) throws Exception {
        singleConnectionPool.returnObject(connection);
    }
}
