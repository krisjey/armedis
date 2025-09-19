
package com.github.armedis.redis.command.management;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisSlowlogRequest extends RedisRequest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Param("size")
    protected Optional<Integer> size;

    public RedisSlowlogRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("Slowlog");
        this.setSubCommand("get");
    }

    public Optional<Integer> getSize() {
        return this.size;
    }

    public void setSize(Optional<Integer> size) {
        this.size = size;
    }
}
