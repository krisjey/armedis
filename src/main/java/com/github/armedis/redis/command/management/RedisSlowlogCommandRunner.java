
package com.github.armedis.redis.command.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RequestRedisCommandName;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

@Component
@Scope("prototype")
@RequestRedisCommandName(RedisCommandEnum.CONFIG)
public class RedisSlowlogCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisSlowlogCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisSlowlogCommandRunner.class);

    private RedisSlowlogRequest redisRequest;

    public RedisSlowlogCommandRunner(RedisSlowlogRequest redisRequest) {
        this.redisRequest = redisRequest;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
        logger.info(redisRequest.toString());
        Integer size = redisRequest.getSize().orElse(10);

        var result = commands.slowlogGet(size);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result, Object.class);
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
        logger.info(redisRequest.toString());
        Integer size = redisRequest.getSize().orElse(10);

        var result = commands.slowlogGet(size);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result, Object.class);
    }
}
