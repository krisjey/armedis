
package com.github.armedis.redis.command.string;

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
@RequestRedisCommandName(RedisCommandEnum.SET)
public class RedisSetCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisSetCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisSetCommandRunner.class);

    private RedisSetRequest redisRequest;

    private RedisTemplate<String, Object> redisTemplate;

    public RedisSetCommandRunner(RedisSetRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        String value = this.redisRequest.getValue();

        this.redisTemplate.opsForValue().set(key, value);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult("OK");
    }

//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
//
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        String value = this.redisRequest.getValue();
//
//        String result = commands.set(key, value);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }
//
//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        String value = this.redisRequest.getValue();
//        String result = commands.set(key, value);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }
}
