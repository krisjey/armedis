
package com.github.armedis.redis.connection;

import static java.util.Objects.requireNonNull;

import java.time.Duration;

import com.github.armedis.redis.RedisNode;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisSeedConnector {
    private RedisNode seedRedisNodeInfo;

    public RedisSeedConnector(RedisNode seedRedisNodeInfo) {
        this.seedRedisNodeInfo = requireNonNull(seedRedisNodeInfo, "Redis seed node info");
    }

    public StatefulRedisConnection<String, String> connect() {
        RedisClient client = RedisClient.create(this.seedRedisNodeInfo.getUri());
        client.setDefaultTimeout(Duration.ofSeconds(2));
        return client.connect();
    }
}
