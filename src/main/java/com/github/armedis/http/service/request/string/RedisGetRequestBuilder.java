
package com.github.armedis.http.service.request.string;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.armedis.http.service.request.BaseRedisRequestBuilder;
import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.redis.command.string.RedisGetRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;

/**
 * 
 * @author krisjey
 */
public class RedisGetRequestBuilder extends BaseRedisRequestBuilder {
    private static final String COMMAND_NAME = "get";

    public RedisGetRequestBuilder() {
        super(COMMAND_NAME);
    }

    @Override
    public RedisRequest build(AggregatedHttpRequest httpRequest, JsonNode jsonBody) {
        return build(httpRequest, jsonBody, getKeyFromJsonNode(jsonBody));
    }

    @Override
    public RedisRequest build(AggregatedHttpRequest httpRequest, JsonNode jsonBody, String key) {
        RedisGetRequest redisRequest = new RedisGetRequest(httpRequest);
        redisRequest.setKey(Optional.of(key));

        return redisRequest;
    }
}
