
package com.github.armedis.redis.command.hash;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisHdelRequest extends RedisRequest {
    @Param("field")
    protected String field;

    public RedisHdelRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("Hdel");
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
}
