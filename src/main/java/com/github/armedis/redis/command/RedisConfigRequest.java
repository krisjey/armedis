
package com.github.armedis.redis.command;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.annotation.Nullable;
import com.linecorp.armeria.server.annotation.Param;

public class RedisConfigRequest extends RedisRequest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * README
     * 값의 존재가 가변일 때 
     * Nullable 또는 Optional<String> 또는 @Default 처리.
     * @Nullable com.linecorp.armeria.common.annotation.Nullable
     * @Default com.linecorp.armeria.server.annotation.Default
     * @Optional java.util.Optional
     */
    @Nullable
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
