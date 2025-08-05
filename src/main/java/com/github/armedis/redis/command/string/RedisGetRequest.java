
package com.github.armedis.redis.command.string;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;

public class RedisGetRequest extends RedisRequest {
    public RedisGetRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
//        // created object by every http request
//        logger.info("Created request " + RedisGetRequest.class.getName());
        this.setCommand("Get");
    }
}
