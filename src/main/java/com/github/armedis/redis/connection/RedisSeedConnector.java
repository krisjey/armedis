package com.github.armedis.redis.connection;

import java.time.Duration;

import com.github.armedis.redis.RedisNode;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisSeedConnector {
    private RedisNode redisInstance;

    public RedisSeedConnector(RedisNode redisInstance) {
        this.redisInstance = redisInstance;
    }

    public StatefulRedisConnection<String, String> connect() {
        RedisClient client = RedisClient.create(this.redisInstance.getUri());
        client.setDefaultTimeout(Duration.ofSeconds(2));
        return client.connect();
    }
}
