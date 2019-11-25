
package com.github.armedis.redis.command;

public class RedisCommandExecuteResultBuilder {
    public enum ResultType {
        STRING,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE;
    }

    private int intResult;
    private String stringResult;
    private float floatResult;
    private long longResult;
    private double doubleResult;
    private ResultType resultType;

    public RedisCommandExecuteResultBuilder(ResultType resultType) {
        this.resultType = resultType;
    }

    public RedisCommandExecuteResultBuilder setResult(int intResult) {
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

    public RedisCommandExecuteResult build() {
        RedisCommandExecuteResult result = new RedisCommandExecuteResultImpl(resultType,
                intResult,
                stringResult,
                floatResult,
                longResult,
                doubleResult);

        return result;
    }
}
