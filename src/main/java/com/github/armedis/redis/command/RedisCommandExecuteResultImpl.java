
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
                result.put("result", boolResult);
                break;
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

            case MAP:
                result.set("result", mapper.convertValue(mapResult, ObjectNode.class));
                break;
                
            case LIST:
                result.set("result", mapper.convertValue(listResult, ArrayNode.class));
                break;

            default:
                result.put("result", stringResult);
                break;
        }

        return result;
    }

}
