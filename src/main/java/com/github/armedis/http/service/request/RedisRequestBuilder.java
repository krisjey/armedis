
package com.github.armedis.http.service.request;

import com.fasterxml.jackson.databind.JsonNode;

public interface RedisRequestBuilder {

    RedisRequest build(JsonNode jsonBody);

    RedisRequest build(JsonNode jsonBody, String key);
}
