package com.github.armedis.redis.connection.pool;

import io.lettuce.core.api.StatefulRedisConnection;

public interface RedisConnectionPool<K, V> {
    StatefulRedisConnection<K, V> getConnection();
}
