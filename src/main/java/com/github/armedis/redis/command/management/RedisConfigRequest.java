
package com.github.armedis.redis.command.management;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisConfigRequest extends RedisRequest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Param("value")
    private Optional<String> value;

    public RedisConfigRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("Config");
    }

    public Optional<String> getValue() {
        return this.value;
    }

    public void setValue(Optional<String> value) {
        this.value = value;
    }
}
