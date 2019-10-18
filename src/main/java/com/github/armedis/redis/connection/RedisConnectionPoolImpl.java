
package com.github.armedis.redis.connection;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.connection.pool.RedisConnectionPool;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.support.ConnectionPoolSupport;

@Component
@DependsOn(value = { "configuratedRedisServerInfo" })
public class RedisConnectionPoolImpl implements RedisConnectionPool<String, String> {
    @Autowired
    @Qualifier("configuratedRedisServerInfo")
    private RedisServerInfo redisServerInfo;

    private GenericObjectPool<StatefulRedisClusterConnection<String, String>> connectionPool = buildConnectionPool();

    private GenericObjectPool<StatefulRedisClusterConnection<String, String>> buildConnectionPool() {
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(true)
                .refreshPeriod(Duration.ofSeconds(5))
                .enableAllAdaptiveRefreshTriggers()
                .build();

//        // FIXME redisConnectionInfo is null
//        Set<RedisNode> nodes = redisConnectionInfo.getRedisNodes();

        // FIXME redisConnectionInfo autowired not working so just work around.
        String address = "192.168.56.104:7003";
        String[] splitAddress = address.split("[:]");
        RedisURI clusterNode = RedisURI.create(splitAddress[0], Integer.parseInt(splitAddress[1]));

        System.out.println(redisServerInfo);

        // cluster node
//        RedisURI clusterNode = null;
//        for (RedisNode item : nodes) {
//            clusterNode = RedisURI.create(item.getHost(), item.getPort());
//        }

//        String address = this.armedisConfiguration.getRedisSeedAddress();
//        String[] splitAddress = address.split("[:]");
//
//        RedisURI clusterNode = RedisURI.create(splitAddress[0], Integer.parseInt(splitAddress[1]));

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
