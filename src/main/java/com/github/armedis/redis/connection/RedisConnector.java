
package com.github.armedis.redis.connection;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.redis.RedisNode;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisConnector implements AutoCloseable {
    private RedisNode redisNode;
    private RedisClient client;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Map<String, StatefulRedisConnection<String, String>> REGISTRY = new ConcurrentHashMap<>();

    public RedisConnector(RedisNode redisNode) {
        this.redisNode = requireNonNull(redisNode, "Redis seed node info");
    }

    public StatefulRedisConnection<String, String> connect() {
        StatefulRedisConnection<String, String> connection = REGISTRY.get(this.redisNode.toString());

        if (connection == null) {
            logger.info(redisNode.toString() + " create new connection");
            this.redisNode.getUri().setTimeout(Duration.ofSeconds(2));
            client = RedisClient.create(this.redisNode.getUri());

            this.client.setOptions(ClientOptions.builder()
                    .autoReconnect(true)
                    .socketOptions(SocketOptions.builder()
                            .keepAlive(true)
                            .tcpNoDelay(true)
                            .build())
                    .build());
            connection = client.connect();

            REGISTRY.put(this.redisNode.toString(), connection);
        }

        return connection;

    }

    @Override
    public void close() {
        // do nothing.
    }
}
