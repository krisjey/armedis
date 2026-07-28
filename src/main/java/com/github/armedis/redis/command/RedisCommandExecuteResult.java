
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
        return new EmptyResult(responseString);
    }

    final class EmptyResult implements RedisCommandExecuteResult {
        private final String responseString;

        // 가능하면 외부에서 주입된 ObjectMapper 사용 권장 (아래 설명)
        private static final ObjectMapper MAPPER = new ObjectMapper();

        EmptyResult(String responseString) {
            this.responseString = responseString;
        }

        @Override
        public String toResponseString() {
            return responseString;
        }

        @Override
        public ObjectNode toObjectNode() {
            ObjectNode node = MAPPER.createObjectNode();
            node.put(RESULT_KEY, responseString);
            return node;
        }
    }
}
