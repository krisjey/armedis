package com.github.armedis.redis.pool;

import io.lettuce.core.api.StatefulRedisConnection;

public interface RedisConnectionPool<K, V> {
    StatefulRedisConnection<K, V> getConnection();
}
