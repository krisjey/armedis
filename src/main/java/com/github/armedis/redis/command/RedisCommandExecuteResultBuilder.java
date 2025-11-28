
package com.github.armedis.redis.command;

import java.util.List;
import java.util.Map;

public class RedisCommandExecuteResultBuilder {
    public enum ResultType {
        STRING,
        BOOLEAN,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        MAP,
        LIST;
    }

    private boolean boolResult;
    private int intResult;
    private String stringResult;
    private float floatResult;
    private long longResult;
    private double doubleResult;
    private Map<Object, Object> mapResult;
    private List<?> listResult;
    private ResultType resultType;

    private Class clazz;
//    private Object objectResult;

    public RedisCommandExecuteResultBuilder(ResultType resultType) {
        this.resultType = resultType;
    }

    public RedisCommandExecuteResultBuilder setResult(boolean boolResult) {
        this.boolResult = boolResult;
        return this;
    }

    public RedisCommandExecuteResultBuilder setResult(int intResult) {
        this.intResult = intResult;
        return this;
    }

    public RedisCommandExecuteResultBuilder setResult(String stringResult) {
        this.stringResult = stringResult;
        return this;
    }

    public RedisCommandExecuteResultBuilder setResult(float floatResult) {
        this.floatResult = floatResult;
        return this;
    }

    public RedisCommandExecuteResultBuilder setResult(long longResult) {
        this.longResult = longResult;
        return this;
    }

    public RedisCommandExecuteResultBuilder setResult(double doubleResult) {
        this.doubleResult = doubleResult;
        return this;
    }

    public RedisCommandExecuteResultBuilder setResult(Map<Object, Object> mapResult) {
        this.mapResult = mapResult;
        return this;
    }

    public RedisCommandExecuteResultBuilder setResult(List<?> listResult, Class clazz) {
        this.listResult = listResult;
        return this;
    }

//    public RedisCommandExecuteResultBuilder setResult(Object objectResult) {
//        this.objectResult = objectResult;
//        return this;
//    }

    public RedisCommandExecuteResult build() {
//        LettuceResponseNormalizer normalize = new LettuceResponseNormalizer();
//        normalize.wrap(objectResult);
        RedisCommandExecuteResult result = new RedisCommandExecuteResultImpl(resultType,
                boolResult,
                intResult,
                stringResult,
                floatResult,
                longResult,
                doubleResult,
                mapResult,
                listResult,
                clazz);

        return result;
    }
}
