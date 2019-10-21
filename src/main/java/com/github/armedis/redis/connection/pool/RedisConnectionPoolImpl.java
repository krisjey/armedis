
package com.github.armedis.redis.connection.pool;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.RedisServerInfoMaker;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.support.ConnectionPoolSupport;

@Component
public class RedisConnectionPoolImpl implements RedisConnectionPool<String, String> {
    private RedisServerInfoMaker redisServerInfoMaker;

    private GenericObjectPool<StatefulRedisClusterConnection<String, String>> connectionPool;

    @Autowired
    public RedisConnectionPoolImpl(RedisServerInfoMaker redisServerInfoMaker) {
        this.redisServerInfoMaker = redisServerInfoMaker;
        this.connectionPool = buildConnectionPool();
    }

    private GenericObjectPool<StatefulRedisClusterConnection<String, String>> buildConnectionPool() {
        requireNonNull(redisServerInfoMaker, "redis server info is null");
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(true)
                .refreshPeriod(Duration.ofSeconds(5))
                .enableAllAdaptiveRefreshTriggers()
                .build();

        Set<RedisNode> nodes = redisServerInfoMaker.detectRedisServer().getRedisNodes();

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
    public StatefulRedisClusterConnection<String, String> getConnection() throws Exception {
        StatefulRedisClusterConnection<String, String> connection = null;

        connection = connectionPool.borrowObject();

        return connection;
    }
}
