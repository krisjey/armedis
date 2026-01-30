
package com.github.armedis.redis.command.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;

public class RedisCommandStatsRequest extends RedisRequest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RedisCommandStatsRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("CommandStats");
        this.setSubCommand("list");
    }
}
