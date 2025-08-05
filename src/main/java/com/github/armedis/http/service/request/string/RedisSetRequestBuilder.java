
package com.github.armedis.http.service.request.string;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.armedis.http.service.request.BaseRedisRequestBuilder;
import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.redis.command.string.RedisSetRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;

/**
 * 
 * @author krisjey
 */
public class RedisSetRequestBuilder extends BaseRedisRequestBuilder {
    private static final String COMMAND_NAME = "set";

    public RedisSetRequestBuilder() {
        super(COMMAND_NAME);
    }

    @Override
    public RedisRequest build(AggregatedHttpRequest httpRequest, JsonNode jsonBody) {
        return build(httpRequest, jsonBody, getKeyFromJsonNode(jsonBody));
    }

    @Override
    public RedisRequest build(AggregatedHttpRequest httpRequest, JsonNode jsonBody, String key) {
        RedisSetRequest redisRequest = new RedisSetRequest(httpRequest);
        redisRequest.setKey(Optional.of(key));

        JsonNode node = jsonBody.get("value");
        if (node.isNull()) {
            throw new RuntimeException("Can not found key from request json");
        }

        redisRequest.setValue(node.asText());

        return redisRequest;
    }
}
