package com.github.armedis.redis;

import java.time.Duration;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

public class ConnectionDetector {
    private RedisInstance redisInstance;

    public ConnectionDetector(RedisInstance redisInstance) {
        this.redisInstance = redisInstance;
    }

    public StatefulRedisConnection<String, String> connect() {
        RedisClient client = RedisClient.create(this.redisInstance.getUri());
        client.setDefaultTimeout(Duration.ofSeconds(2));
        return client.connect();
    }

}
