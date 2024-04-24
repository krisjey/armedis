
package com.github.armedis.redis.command;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisSetRequest extends RedisRequest {
    @Param("value")
    protected String value;

    public RedisSetRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("Set");
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
