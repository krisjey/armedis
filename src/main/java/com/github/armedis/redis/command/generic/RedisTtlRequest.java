
package com.github.armedis.redis.command.generic;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;

public class RedisTtlRequest extends RedisRequest {
    public RedisTtlRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
//        // created object by every http request
//        logger.info("Created request " + RedisTtlRequest.class.getName());
        this.setCommand("Ttl");
    }
}
