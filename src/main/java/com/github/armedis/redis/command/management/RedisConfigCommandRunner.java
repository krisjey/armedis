
package com.github.armedis.redis.command.management;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.github.armedis.config.RedisConfigManager;
import com.github.armedis.http.service.management.configs.AllowedConfigCommands;
import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RequestRedisCommandName;
import com.linecorp.armeria.common.HttpMethod;

@Component
@Scope("prototype")
@RequestRedisCommandName(RedisCommandEnum.CONFIG)
public class RedisConfigCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisConfigCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisConfigCommandRunner.class);

    private RedisConfigRequest redisRequest;

    @Autowired
    private RedisConfigManager redisConfigManager;

    public RedisConfigCommandRunner(RedisConfigRequest redisRequest, RedisTemplate<String, Object> redisTemplate) {
        this.redisRequest = redisRequest;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet() {
        logger.info(redisRequest.toString() + "][" + redisRequest.getValue());

        String key = this.redisRequest.getKey();
        Map<Object, Object> result = new HashMap<>();

        if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
            String tempResult = this.redisConfigManager.getConfigValue(key);
            result.put(key, String.valueOf(tempResult));
            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
        }
        else {
            Optional<String> value = this.redisRequest.getValue();

            this.redisConfigManager.setConfigValue(key, value.get());

            // 업데이트 시 내부 값 업데이트
            AllowedConfigCommands.get(key).setCurrentValueFromDB(value.get());

            // String simple-string-reply: OK when the configuration was set properly.
            // connection.sync().configSet("", "");
            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult("OK");
        }
    }
}
