
package com.github.armedis.redis.command.hash;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;

public class RedisHlenRequest extends RedisRequest {
    public RedisHlenRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("Hlen");
    }
}
