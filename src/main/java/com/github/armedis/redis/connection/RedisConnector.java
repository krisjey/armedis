
package com.github.armedis.redis.connection;

import static java.util.Objects.requireNonNull;

import java.time.Duration;

import com.github.armedis.redis.RedisNode;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisConnector implements AutoCloseable {
    private RedisNode redisNode;
    private RedisClient client;

    public RedisConnector(RedisNode redisNode) {
        this.redisNode = requireNonNull(redisNode, "Redis seed node info");
    }

    public StatefulRedisConnection<String, String> connect() {
        this.redisNode.getUri().setTimeout(Duration.ofSeconds(2));
        client = RedisClient.create(this.redisNode.getUri());

        return client.connect();
    }

    @Override
    public void close() throws Exception {
        client.close();
    }
}
