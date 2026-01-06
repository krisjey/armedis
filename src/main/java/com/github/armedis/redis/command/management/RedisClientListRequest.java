
package com.github.armedis.redis.command.management;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisClientListRequest extends RedisRequest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Param("size")
    private Optional<Integer> size;

    public RedisClientListRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("ClientList");
        this.setSubCommand("list");
    }
}
