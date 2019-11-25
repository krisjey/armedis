
package com.github.armedis.http.service.request;

import static java.util.Objects.requireNonNull;

import com.github.armedis.http.service.request.string.RedisGetRequestBuilder;
import com.github.armedis.redis.command.RedisCommandEnum;

public class RedisRequestBuilderFactory {

    public static RedisRequestBuilder createRedisRequestBuilder(String command) {
        RedisCommandEnum redisCommandName = RedisCommandEnum.of(command);

        RedisRequestBuilder builder = null;

        switch (redisCommandName) {
            case GET:
                builder = new RedisGetRequestBuilder();
                break;

            default:
                break;
        }

        requireNonNull(builder, "Builder should be not null!");

        return builder;
    }

}
