
package com.github.armedis.redis.command.hash;

import java.util.List;

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
public class RedisHttlCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisHttlCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisHttlCommandRunner.class);

    private RedisHttlRequest redisRequest;

    public RedisHttlCommandRunner(RedisHttlRequest redisRequest) {
        this.redisRequest = redisRequest;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {

        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        String field = this.redisRequest.getField();
        List<Long> result = commands.httl(key, field);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        String field = this.redisRequest.getField();
        List<Long> result = commands.httl(key, field);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }
}
