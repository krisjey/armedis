package com.github.armedis.redis.command.management;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.github.armedis.redis.RedisNode;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

/**
 * Execute command to each RedisNode
 */
@Component
public class LettuceRedisNodeCommandExecutor {

    private final Duration connectionTimeout;
    private final Duration commandTimeout;
    private final String password;

    public LettuceRedisNodeCommandExecutor(Duration connectionTimeout, Duration commandTimeout, String password) {
        this.connectionTimeout = connectionTimeout;
        this.commandTimeout = commandTimeout;
        this.password = password;
    }

    public <T> Map<RedisNode, T> executeOnEach(Collection<RedisNode> nodes, Function<RedisCommands<String, String>, T> callback) {
        Map<RedisNode, T> result = new LinkedHashMap<>();
        for (RedisNode node : nodes) {
            T value = executeInternal(node, callback, false);
            result.put(node, value);
        }
        return result;
    }

    private <T> T executeInternal(RedisNode node, Function<RedisCommands<String, String>, T> callback, boolean allowRetry) {

        RedisClient client = null;
        StatefulRedisConnection<String, String> connection = null;

        try {
            RedisURI uri = RedisURI.builder()
                    .withHost(node.getHost()) // RedisNode에 맞게 수정
                    .withPort(node.getPort())
                    .withTimeout(connectionTimeout)
                    .build();

            if (password != null && !password.isBlank()) {
                uri.setPassword(password.toCharArray());
            }

            client = RedisClient.create(uri);
            connection = client.connect();
            connection.setTimeout(commandTimeout);

            RedisCommands<String, String> commands = connection.sync();
            return callback.apply(commands);
        }
        catch (Exception e) {
            // 1회 재시도 후에도 실패하면 예외 전파
            if (allowRetry) {
                closeQuietly(connection);
                shutdownQuietly(client);
                return executeInternal(node, callback, false);
            }
            throw new RedisNodeCommandException(
                    String.format("Failed to execute command on node %s:%d",
                            node.getHost(), node.getPort()),
                    e);
        }
        finally {
            closeQuietly(connection);
            shutdownQuietly(client);
        }
    }

    private void closeQuietly(StatefulRedisConnection<?, ?> connection) {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (Exception ignored) {
            }
        }
    }

    private void shutdownQuietly(RedisClient client) {
        if (client != null) {
            try {
                client.shutdown();
            }
            catch (Exception ignored) {
            }
        }
    }

    public static class RedisNodeCommandException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public RedisNodeCommandException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}