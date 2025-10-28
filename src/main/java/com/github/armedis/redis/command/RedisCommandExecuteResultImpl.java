
package com.github.armedis.redis.command;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.redis.command.RedisCommandExecuteResultBuilder.ResultType;

public class RedisCommandExecuteResultImpl implements RedisCommandExecuteResult {
    static final ObjectMapper mapper = new ObjectMapper();

    private boolean boolResult;
    private int intResult;
    private String stringResult;
    private float floatResult;
    private long longResult;
    private double doubleResult;
    private Map<String, String> mapResult;
    private List<?> listResult;
    private ResultType resultType;
    private Class clazz;

    public RedisCommandExecuteResultImpl(ResultType resultType, boolean boolResult, int intResult, String stringResult, float floatResult, long longResult, double doubleResult, Map<String, String> mapResult, List<?> listResult, Class clazz) {
        this.resultType = resultType;
        this.boolResult = boolResult;
        this.intResult = intResult;
        this.stringResult = stringResult;
        this.floatResult = floatResult;
        this.longResult = longResult;
        this.doubleResult = doubleResult;
        this.mapResult = mapResult;
        this.listResult = listResult;
        this.clazz = clazz;
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
            case BOOLEAN:
                result.put(RedisCommandExecuteResult.RESULT_KEY, boolResult);
                break;
            case INTEGER:
                result.put(RedisCommandExecuteResult.RESULT_KEY, intResult);
                break;

            case LONG:
                result.put(RedisCommandExecuteResult.RESULT_KEY, longResult);
                break;
            case FLOAT:
                result.put(RedisCommandExecuteResult.RESULT_KEY, floatResult);
                break;

            case DOUBLE:
                result.put(RedisCommandExecuteResult.RESULT_KEY, doubleResult);
                break;

            case MAP:
                result.set(RedisCommandExecuteResult.RESULT_KEY, mapper.convertValue(mapResult, ObjectNode.class));
                break;
                
            case LIST:
                result.set(RedisCommandExecuteResult.RESULT_KEY, mapper.convertValue(listResult, ArrayNode.class));
                break;

            default:
                result.put(RedisCommandExecuteResult.RESULT_KEY, stringResult);
                break;
        }

        return result;
    }

}
