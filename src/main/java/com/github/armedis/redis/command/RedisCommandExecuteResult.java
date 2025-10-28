
package com.github.armedis.redis.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface RedisCommandExecuteResult {
    static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Convert redis execute result to response data. 
     * @return 
     */
    String toResponseString();

    /**
     * Convert redis execute result to response data. 
     * @return
     */
    ObjectNode toObjectNode();

    public static final String RESULT_KEY = "result";

    static RedisCommandExecuteResult getEmptyResult(String responseString) {
        return new RedisCommandExecuteResult() {

            @Override
            public String toResponseString() {
                return responseString;
            }

            @Override
            public ObjectNode toObjectNode() {
                return mapper.createObjectNode();
            }
        };
    }
}
