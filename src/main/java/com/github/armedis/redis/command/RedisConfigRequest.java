
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
     * README @Optional java.util.Optional은 필드가 존재하지 않을 때 400 오류 발생함
     * 값의 존재가 가변일 때는 Nullable 또는 @Default 처리.
     * @Nullable com.linecorp.armeria.common.annotation.Nullable
     * @Default com.linecorp.armeria.server.annotation.Default
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
