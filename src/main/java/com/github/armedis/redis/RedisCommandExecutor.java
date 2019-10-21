
package com.github.armedis.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.redis.connection.RedisConnectionFactory;

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

@Component
public class RedisCommandExecutor {
    private final Logger logger = LoggerFactory.getLogger(RedisCommandExecutor.class);

    @Autowired
    private RedisConnectionFactory factory;

    public ObjectNode execute(RedisRequest redisRequest) throws Exception {
        StatefulRedisClusterConnection<String, String> connection = factory.getConnection();
        RedisAdvancedClusterCommands<String, String> commands = connection.sync();

        // RedisConnectionFactory로부터 레디스 sync command를 가져온다.
        // RedisRequest로 redis command 객체를 가져온다.

//        RedidCommandLookup.getCommand(redisRequest);
        commands.get("hello:kris2");
        logger.info(commands.get("hello:kris2"));

        logger.info("Command execute with redisRequest" + redisRequest.toString());

        // FIXME should be impl.
        return null;
    }
}
