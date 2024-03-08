
package com.github.armedis.redis.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisConfigRequest extends RedisRequest {
    private final Logger logger = LoggerFactory.getLogger(RedisConfigRequest.class);

    private static final String COMMAND_NAME = "set";

    @Param("value")
    protected String value;

    public RedisConfigRequest() {
//        // created object by every http request
//        logger.info("Created request " + RedisGetRequest.class.getName());
        this.setCommand(COMMAND_NAME);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
