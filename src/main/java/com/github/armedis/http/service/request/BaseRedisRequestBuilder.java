
package com.github.armedis.http.service.request;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class BaseRedisRequestBuilder implements RedisRequestBuilder {
    private String command;

    public BaseRedisRequestBuilder(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    protected String getKeyFromJsonNode(JsonNode jsonBody) {
        JsonNode node = jsonBody.get("key");
        if (node.isNull()) {
            throw new RuntimeException("Can not found key from request json");
        }

        return node.asText();
    }

}
