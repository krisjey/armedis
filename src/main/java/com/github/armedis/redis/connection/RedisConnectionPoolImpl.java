
package com.github.armedis.redis.connection;

import java.util.concurrent.TimeUnit;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.connection.pool.RedisConnectionPool;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.support.ConnectionPoolSupport;

@Component
public class RedisConnectionPoolImpl implements RedisConnectionPool<String, String> {
    @Autowired
    private RedisServerInfo redisConnectionInfo;

    private void buildConnectionPool() {
        RedisClusterClient clusterClient = RedisClusterClient.create(clusterNodes);
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(true)
                .refreshPeriod(5, TimeUnit.SECONDS)
                .enableAllAdaptiveRefreshTriggers()
                .build();

        clusterClient.setOptions(ClusterClientOptions.builder()
                .topologyRefreshOptions(topologyRefreshOptions)
                .build());

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxTotal(10);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setBlockWhenExhausted(true);

        pool = ConnectionPoolSupport.createGenericObjectPool(clusterClient::connect, poolConfig);
    }

    @Override
    public StatefulRedisConnection<String, String> getConnection() {
        // TODO Auto-generated method stub
        return null;
    }

}
