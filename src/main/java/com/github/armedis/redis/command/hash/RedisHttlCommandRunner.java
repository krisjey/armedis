
package com.github.armedis.redis.command.hash;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
        List<String> field = this.redisRequest.getField();
        List<Entry<Object, Duration>> result = this.redisTemplate.opsForHash().getTimeToLive(key, new ArrayList<Object>(field)).expiring();
        Map<Object, Object> ttlSecondsMap = result.stream()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        e -> e.getValue().getSeconds()));
        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(ttlSecondsMap);
    }
}
