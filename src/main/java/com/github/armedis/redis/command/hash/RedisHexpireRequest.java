
package com.github.armedis.redis.command.hash;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisHexpireRequest extends RedisRequest {
    @Param("field")
    protected String field;
    
    @Param("seconds")
    protected Long seconds;
    
    public RedisHexpireRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("Hexpire");
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
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
