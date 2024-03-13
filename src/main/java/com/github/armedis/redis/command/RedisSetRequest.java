
package com.github.armedis.redis.command;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisSetRequest extends RedisRequest {
    @Param("value")
    protected String value;

    public RedisSetRequest() {
//        // created object by every http request
//        logger.info("Created request " + RedisGetRequest.class.getName());
        this.setCommand(getCommand());
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
