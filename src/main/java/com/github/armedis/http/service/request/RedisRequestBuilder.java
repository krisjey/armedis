
package com.github.armedis.http.service.request;

import com.fasterxml.jackson.databind.JsonNode;

public interface RedisRequestBuilder {

    public RedisRequest build(JsonNode jsonBody);

    public RedisRequest build(JsonNode jsonBody, String key);
}
