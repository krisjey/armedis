
package com.github.armedis.redis.command.generic;

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
// TODO Sum 계산시 사용하는 VO 설정 필요. 현재 uptime 등 값이 0으로 내려감.
@RequestRedisCommandName(RedisCommandEnum.EXPIRE)
public class RedisExpireCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisExpireCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisExpireCommandRunner.class);

    private RedisExpireRequest redisRequest;

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisExpireCommandRunner(RedisExpireRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
        this.redisTemplate = redisTemplate;
    }

//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
//
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        Long seconds = this.redisRequest.getSeconds();
//        Boolean result = commands.expire(key, seconds);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }
//
//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        Long seconds = this.redisRequest.getSeconds();
//        Boolean result = commands.expire(key, seconds);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        logger.info(redisRequest.toString());
        String key = this.redisRequest.getKey();
        Long seconds = this.redisRequest.getSeconds();

        Boolean result = redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

}
