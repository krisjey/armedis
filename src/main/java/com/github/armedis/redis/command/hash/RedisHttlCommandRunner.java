
package com.github.armedis.redis.command.hash;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expirations.TimeToLive;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RequestRedisCommandName;

@Component
@Scope("prototype")
@RequestRedisCommandName(RedisCommandEnum.HTTL)
public class RedisHttlCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisHttlCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisHttlCommandRunner.class);

    private RedisHttlRequest redisRequest;

    private RedisTemplate<String, Object> redisTemplate;

    public RedisHttlCommandRunner(RedisHttlRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        String field = this.redisRequest.getField();
        List<TimeToLive> result = this.redisTemplate.opsForHash().getTimeToLive(key, List.of(field)).ttl();

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result, Long.class);
    }

//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
//
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        String field = this.redisRequest.getField();
//        List<Long> result = commands.httl(key, field);
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
//        List<Long> result = commands.httl(key, field);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result, Long.class);
//    }
}
