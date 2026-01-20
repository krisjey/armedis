
package com.github.armedis.redis.command.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;

public class RedisNodeListRequest extends RedisRequest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RedisNodeListRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("NodeList");
        this.setSubCommand("list");
    }
}
