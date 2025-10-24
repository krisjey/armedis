
package com.github.armedis.redis.command.generic;

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
// TODO Sum 계산시 사용하는 VO 설정 필요. 현재 uptime 등 값이 0으로 내려감.
@RequestRedisCommandName(RedisCommandEnum.EXPIRE)
public class RedisExpireCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisExpireCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisExpireCommandRunner.class);

    private RedisExpireRequest redisRequest;

    public RedisExpireCommandRunner(RedisExpireRequest redisRequest) {
        this.redisRequest = redisRequest;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {

        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        Long seconds = this.redisRequest.getSeconds();
        Boolean result = commands.expire(key, seconds);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        Long seconds = this.redisRequest.getSeconds();
        Boolean result = commands.expire(key, seconds);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }
}
