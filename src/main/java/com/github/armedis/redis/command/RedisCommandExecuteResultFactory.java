
package com.github.armedis.redis.command;

import java.util.List;
import java.util.Map;

import com.github.armedis.redis.command.RedisCommandExecuteResultBuilder.ResultType;

public class RedisCommandExecuteResultFactory {

    public static RedisCommandExecuteResult buildRedisCommandExecuteResult(String result) {

        RedisCommandExecuteResultBuilder builder = new RedisCommandExecuteResultBuilder(ResultType.STRING);
        builder = builder.setResult(result);

        return builder.build();
    }
    
    public static RedisCommandExecuteResult buildRedisCommandExecuteResult(Boolean result) {

        RedisCommandExecuteResultBuilder builder = new RedisCommandExecuteResultBuilder(ResultType.BOOLEAN);
        builder = builder.setResult(result);

        return builder.build();
    }

    public static RedisCommandExecuteResult buildRedisCommandExecuteResult(Integer result) {

        RedisCommandExecuteResultBuilder builder = new RedisCommandExecuteResultBuilder(ResultType.INTEGER);
        builder = builder.setResult(result);

        return builder.build();
    }

    public static RedisCommandExecuteResult buildRedisCommandExecuteResult(Long result) {

        RedisCommandExecuteResultBuilder builder = new RedisCommandExecuteResultBuilder(ResultType.LONG);
        builder = builder.setResult(result);

        return builder.build();
    }

    public static RedisCommandExecuteResult buildRedisCommandExecuteResult(Float result) {

        RedisCommandExecuteResultBuilder builder = new RedisCommandExecuteResultBuilder(ResultType.FLOAT);
        builder = builder.setResult(result);

        return builder.build();
    }

    public static RedisCommandExecuteResult buildRedisCommandExecuteResult(Double result) {

        RedisCommandExecuteResultBuilder builder = new RedisCommandExecuteResultBuilder(ResultType.DOUBLE);
        builder = builder.setResult(result);

        return builder.build();
    }
    
    public static RedisCommandExecuteResult buildRedisCommandExecuteResult(Map<String, String> result) {

        RedisCommandExecuteResultBuilder builder = new RedisCommandExecuteResultBuilder(ResultType.MAP);
        builder = builder.setResult(result);

        return builder.build();
    }
    
    public static RedisCommandExecuteResult buildRedisCommandExecuteResult(List<Long> result) {

        RedisCommandExecuteResultBuilder builder = new RedisCommandExecuteResultBuilder(ResultType.LIST);
        builder = builder.setResult(result);

        return builder.build();
    }
}
