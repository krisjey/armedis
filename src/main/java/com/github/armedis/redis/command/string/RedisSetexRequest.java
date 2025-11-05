
package com.github.armedis.redis.command.string;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisSetexRequest extends RedisRequest {
    @Param("value")
    protected String value;

    @Param("seconds")
    protected Long seconds;

    public RedisSetexRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("Setex");
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the seconds
     */
    public Long getSeconds() {
        return seconds;
    }

    /**
     * @param seconds the seconds to set
     */
    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }
}
