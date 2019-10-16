
package com.github.armedis.redis.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.http.service.request.RedisRequest;

public class RedisGetRequest extends RedisRequest {
    private final Logger logger = LoggerFactory.getLogger(RedisGetRequest.class);

    private static final String COMMAND_NAME = "get";

    public RedisGetRequest() {
//        // created object by every http request
//        logger.info("Created request " + RedisGetRequest.class.getName());
        this.setCommand(COMMAND_NAME);
    }

}
