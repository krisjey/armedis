
package com.github.armedis.redis.command.hash;

import java.util.Map;

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
@RequestRedisCommandName(RedisCommandEnum.GET)
public class RedisHgetallCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisHgetallCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisHgetallCommandRunner.class);

    private RedisHgetallRequest redisRequest;

    public RedisHgetallCommandRunner(RedisHgetallRequest redisRequest) {
        this.redisRequest = redisRequest;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {

        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        Map<String, String> result = commands.hgetall(key);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        Map<String, String> result = commands.hgetall(key);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }
}
