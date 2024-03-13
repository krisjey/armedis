
package com.github.armedis.redis.command;

import com.github.armedis.http.service.request.RedisRequest;

public class RedisGetRequest extends RedisRequest {
    public RedisGetRequest() {
//        // created object by every http request
//        logger.info("Created request " + RedisGetRequest.class.getName());
        this.setCommand(getCommand());
    }
}
