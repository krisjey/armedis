
package com.github.armedis.redis.command.hash;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;

public class RedisHgetRequest extends RedisRequest {
    public RedisHgetRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("Hget");
    }
}
