
package com.github.armedis.redis.command.management;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RequestRedisCommandName;

import io.lettuce.core.api.sync.RedisCommands;

@Component
@Scope("prototype")
@RequestRedisCommandName(RedisCommandEnum.CONFIG)
public class RedisSlowlogCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisSlowlogCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisSlowlogCommandRunner.class);

    private RedisSlowlogRequest redisRequest;

    private RedisTemplate<String, Object> redisTemplate;

    public RedisSlowlogCommandRunner(RedisSlowlogRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        logger.info(redisRequest.toString());
        Integer size = redisRequest.getSize().orElse(10);

        List<Object> result = this.redisTemplate.execute((RedisCallback<List<Object>>) connection -> {
            Object nativeConnection = connection.getNativeConnection();
            if (nativeConnection instanceof RedisCommands) {
                @SuppressWarnings("unchecked")
                RedisCommands<byte[], byte[]> commands = (RedisCommands<byte[], byte[]>) nativeConnection;
                return (List<Object>) (List<?>) commands.slowlogGet(size);
            }
            return null;
        });

        if (result == null) {
            result = new ArrayList<Object>();
        }
        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result, Object.class);
    }
}
