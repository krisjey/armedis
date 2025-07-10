
package com.github.armedis.http.service.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.linecorp.armeria.common.AggregatedHttpRequest;

public interface RedisRequestBuilder {

    RedisRequest build(AggregatedHttpRequest httpRequest, JsonNode jsonBody);

    RedisRequest build(AggregatedHttpRequest httpRequest, JsonNode jsonBody, String key);
}
