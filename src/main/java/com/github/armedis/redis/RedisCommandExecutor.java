
package com.github.armedis.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.http.service.request.RedisRequest;

@Component
public class RedisCommandExecutor {
    private static final Logger logger = LoggerFactory.getLogger(RedisCommandExecutor.class);
    // Autowired redis connection pool
//    @Autowired

    public ObjectNode execute(RedisRequest redisRequest) {
        redisRequest.getCommand();

        logger.info("Command execute with redisRequest" + redisRequest.toString());

        // FIXME should be impl.
        return null;
    }
}
