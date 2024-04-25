
package com.github.armedis.redis.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisConfigRequest extends RedisRequest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Param("value")
    protected String value;

    public RedisConfigRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("Config");
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
