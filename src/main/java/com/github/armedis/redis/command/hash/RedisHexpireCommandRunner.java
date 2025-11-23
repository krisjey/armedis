
package com.github.armedis.redis.command.hash;

import java.time.Duration;
import java.util.List;

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
@RequestRedisCommandName(RedisCommandEnum.HEXPIRE)
public class RedisHexpireCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisHexpireCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisHexpireCommandRunner.class);

    private RedisHexpireRequest redisRequest;

    private RedisTemplate<String, Object> redisTemplate;

    public RedisHexpireCommandRunner(RedisHexpireRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        String field = this.redisRequest.getField();
        Long seconds = this.redisRequest.getSeconds();
        boolean result = this.redisTemplate.opsForHash().expire(key, Duration.ofSeconds(seconds), List.of(field)).allChanged();

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
//
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        String field = this.redisRequest.getField();
//        Long seconds = this.redisRequest.getSeconds();
//        List<Long> result = commands.hexpire(key, seconds, field);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result, Long.class);
//    }
//
//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        String field = this.redisRequest.getField();
//        Long seconds = this.redisRequest.getSeconds();
//        List<Long> result = commands.hexpire(key, seconds, field);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result, Long.class);
//    }
}
