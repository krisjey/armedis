
package com.github.armedis.http.service.request.string;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.armedis.http.service.request.BaseRedisRequestBuilder;
import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.redis.command.RedisGetRequest;

/**
 * 
 * @author krisjey
 */
public class RedisGetRequestBuilder extends BaseRedisRequestBuilder {
    private static final String COMMAND_NAME = "get";

    public RedisGetRequestBuilder(String command) {
        super(command);
    }

    @Override
    public RedisRequest build(JsonNode jsonBody) {
        return build(jsonBody, getKeyFromJsonNode(jsonBody));
    }

    @Override
    public RedisRequest build(JsonNode jsonBody, String key) {
        RedisGetRequest redisRequest = new RedisGetRequest();
        redisRequest.setKey(Optional.of(key));

        return redisRequest;
    }

}
