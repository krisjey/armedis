
package com.github.armedis.redis.connection.pool;

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;

public interface RedisConnectionPool<K, V> {
    StatefulRedisClusterConnection<String, String> getConnection() throws Exception;
}
