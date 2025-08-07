
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
    private Map<String, String> mapResult;
    private List<Long> listResult;
    private ResultType resultType;

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

    public RedisCommandExecuteResultBuilder setResult(Map<String, String> mapResult) {
        this.mapResult = mapResult;
        return this;
    }

    public RedisCommandExecuteResultBuilder setResult(List<Long> listResult) {
        this.listResult = listResult;
        return this;
    }

    public RedisCommandExecuteResult build() {
        RedisCommandExecuteResult result = new RedisCommandExecuteResultImpl(resultType,
                boolResult,
                intResult,
                stringResult,
                floatResult,
                longResult,
                doubleResult,
                mapResult,
                listResult);

        return result;
    }
}
