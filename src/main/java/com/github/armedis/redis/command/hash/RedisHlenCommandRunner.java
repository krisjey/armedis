
package com.github.armedis.redis.command.hash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RequestRedisCommandName;

@Component
@Scope("prototype")
@RequestRedisCommandName(RedisCommandEnum.HLEN)
public class RedisHlenCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisHlenCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisHlenCommandRunner.class);

    private RedisHlenRequest redisRequest;

    private RedisTemplate<String, Object> redisTemplate;

    public RedisHlenCommandRunner(RedisHlenRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        Long result = this.redisTemplate.opsForHash().size(key);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
//
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        Long result = commands.hlen(key);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }
//
//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        Long result = commands.hlen(key);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }
}
