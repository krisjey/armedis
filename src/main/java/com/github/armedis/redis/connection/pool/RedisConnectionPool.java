
package com.github.armedis.redis.connection.pool;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;

public interface RedisConnectionPool<K, V> {
    StatefulRedisClusterConnection<K, V> getClusterConnection() throws Exception;

    StatefulRedisConnection<K, V> getNonClusterConnection() throws Exception;

    void returnObject(StatefulRedisClusterConnection<K, V> connection) throws Exception;

    void returnObject(StatefulRedisConnection<K, V> connection) throws Exception;
}
