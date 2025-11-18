
package com.github.armedis.redis.command.hash;

import java.util.Map;

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

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

@Component
@Scope("prototype")
@RequestRedisCommandName(RedisCommandEnum.HGETALL)
public class RedisHgetallCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisHgetallCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisHgetallCommandRunner.class);

    private RedisHgetallRequest redisRequest;

    private RedisTemplate<String, Object> redisTemplate;

    public RedisHgetallCommandRunner(RedisHgetallRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        Map<Object, Object> result = this.redisTemplate.opsForHash().entries(key);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
//
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        Map<String, String> result = commands.hgetall(key);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }
//
//    @Override
//    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
//        logger.info(redisRequest.toString());
//
//        String key = this.redisRequest.getKey();
//        Map<String, String> result = commands.hgetall(key);
//
//        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
//    }
}
