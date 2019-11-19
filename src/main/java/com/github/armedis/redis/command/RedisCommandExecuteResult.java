
package com.github.armedis.redis.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface RedisCommandExecuteResult {
    static final ObjectMapper mapper = new ObjectMapper();

    String toResponseString();

    ObjectNode toObjectNode();

    static RedisCommandExecuteResult getEmptyResult() {
        return new RedisCommandExecuteResult() {

            @Override
            public String toResponseString() {
                return "500";
            }

            @Override
            public ObjectNode toObjectNode() {
                return mapper.createObjectNode();
            }
        };
    }
}
