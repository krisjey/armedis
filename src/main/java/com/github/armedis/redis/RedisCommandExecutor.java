
package com.github.armedis.redis;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.redis.connection.pool.RedisConnectionPool;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

@Component
public class RedisCommandExecutor {
    private final Logger logger = LoggerFactory.getLogger(RedisCommandExecutor.class);

    private final ObjectMapper mapper = new ObjectMapper();

    private RedisConnectionPool<String, String> redisConnectionPool;

    private RedisInstanceType redisServerInfo;

    @Autowired
    public RedisCommandExecutor(RedisConnectionPool<String, String> redisConnectionPool, RedisServerInfoMaker redisServerInfoMaker) {
        this.redisConnectionPool = redisConnectionPool;
        this.redisServerInfo = redisServerInfoMaker.getRedisServerInfo().getRedisInstanceType();
    }

    public JsonNode execute(RedisRequest redisRequest) throws Exception {
        switch (this.redisServerInfo) {
            case STANDALONE:
            case SENTINEL:
                return executeNonClusterCommand(redisRequest);

            case CLUSTER:
                return executeClusterCommand(redisRequest);

            default:
                throw new NotImplementedException("Connection pool not implemented " + redisServerInfo.toString());
        }
    }

    private JsonNode executeClusterCommand(RedisRequest redisRequest) throws Exception {
        StatefulRedisClusterConnection<String, String> connection = this.redisConnectionPool.getClusterConnection();
        RedisAdvancedClusterCommands<String, String> commands = connection.sync();

        String addValue = "my name is kris";
        // RedisConnectionFactory로부터 레디스 sync command를 가져온다.
        // RedisRequest로 redis command 객체를 가져온다.
        commands.set(redisRequest.getKey().get(), addValue);

        String receivedValue = commands.get(redisRequest.getKey().get());

        logger.info("receivedValue " + receivedValue);
        JsonNode result = mapper.valueToTree(receivedValue);

        logger.info("Command execute with redisRequest" + redisRequest.toString());
        return result;
    }

    private JsonNode executeNonClusterCommand(RedisRequest redisRequest) throws Exception {
        StatefulRedisConnection<String, String> connection = this.redisConnectionPool.getNonClusterConnection();
        RedisCommands<String, String> commands = connection.sync();

        String addValue = "my name is kris";
        // RedisConnectionFactory로부터 레디스 sync command를 가져온다.
        // RedisRequest로 redis command 객체를 가져온다.
        commands.set(redisRequest.getKey().get(), addValue);

        String receivedValue = commands.get(redisRequest.getKey().get());
        logger.info("receivedValue " + receivedValue);

        JsonNode result = mapper.valueToTree(receivedValue);

        logger.info("Command execute with redisRequest" + redisRequest.toString());

        return result;
    }
}
