
package com.github.armedis.redis.command.generic;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisExpireRequest extends RedisRequest {
    @Param("seconds")
    private Long seconds;

    public RedisExpireRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
//        // created object by every http request
//        logger.info("Created request " + RedisExpireRequest.class.getName());
        this.setCommand("Expire");
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
