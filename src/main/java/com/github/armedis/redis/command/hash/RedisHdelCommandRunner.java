
package com.github.armedis.redis.command.hash;

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
@RequestRedisCommandName(RedisCommandEnum.HDEL)
public class RedisHdelCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisHdelCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisHdelCommandRunner.class);

    private RedisHdelRequest redisRequest;

    private RedisTemplate<String, Object> redisTemplate;

    public RedisHdelCommandRunner(RedisHdelRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        String field = this.redisRequest.getField();
        Long result = redisTemplate.opsForHash().delete(key, field);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
//
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        String field = this.redisRequest.getField();
//        Long result = commands.hdel(key, field);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }
//
//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        String field = this.redisRequest.getField();
//        Long result = commands.hdel(key, field);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }
}
