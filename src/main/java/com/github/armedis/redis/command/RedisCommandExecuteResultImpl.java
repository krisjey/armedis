
package com.github.armedis.redis.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.redis.command.RedisCommandExecuteResultBuilder.ResultType;

public class RedisCommandExecuteResultImpl implements RedisCommandExecuteResult {
    static final ObjectMapper mapper = new ObjectMapper();

    private int intResult;
    private String stringResult;
    private float floatResult;
    private long longResult;
    private double doubleResult;
    private ResultType resultType;

    public RedisCommandExecuteResultImpl(ResultType resultType, int intResult, String stringResult, float floatResult, long longResult, double doubleResult) {
        this.resultType = resultType;
        this.intResult = intResult;
        this.stringResult = stringResult;
        this.floatResult = floatResult;
        this.longResult = longResult;
        this.doubleResult = doubleResult;
    }

    @Override
    public String toResponseString() {
        ObjectNode result = createObjectNode();
        return result.textValue();
    }

    @Override
    public ObjectNode toObjectNode() {
        return createObjectNode();
    }

    private ObjectNode createObjectNode() {
        ObjectNode result = mapper.createObjectNode();
        switch (resultType) {
            case INTEGER:
                result.put("result", intResult);
                break;

            case LONG:
                result.put("result", longResult);
                break;
            case FLOAT:
                result.put("result", floatResult);
                break;

            case DOUBLE:
                result.put("result", doubleResult);
                break;

            default:
                result.put("result", stringResult);
                break;
        }

        return result;
    }

}
