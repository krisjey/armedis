
package com.github.armedis.redis.command.string;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RequestRedisCommandName;

@Component
@Scope("prototype")
@RequestRedisCommandName(RedisCommandEnum.SETEX)
public class RedisSetexCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisSetexCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisSetexCommandRunner.class);

    private RedisSetexRequest redisRequest;

    private RedisTemplate<String, Object> redisTemplate;

    public RedisSetexCommandRunner(RedisSetexRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        String value = this.redisRequest.getValue();
        Long seconds = this.redisRequest.getSeconds();

        this.redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult("OK");
    }
}
