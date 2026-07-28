
package com.github.armedis.redis.command;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.redis.connection.RedisServerDetector;

@Component
public class RedisCommandExecutor {
    private final Logger logger = LoggerFactory.getLogger(RedisCommandExecutor.class);

    private RedisTemplate<String, Object> redisTemplate;

    private BeanFactory beanFactory;

    private RedisServerDetector redisServerDetector;

    @Autowired
    public RedisCommandExecutor(RedisTemplate<String, Object> redisTemplate, RedisServerDetector redisServerDetector, BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.redisTemplate = redisTemplate;
        this.redisServerDetector = redisServerDetector;
    }

    public RedisCommandExecuteResult execute(RedisRequest redisRequest) throws Exception {
        String commandRunnerName = RedisCommandRunner.getCommandRunnerName(redisRequest.getCommand());

        Object commandRunner = this.beanFactory.getBean(commandRunnerName, redisRequest, redisTemplate);

        // never enter the condition, There is no constructor with redisRequest class.
        if (commandRunner == null) {
            logger.warn("Can not found request bean name [" + commandRunnerName + "] " + redisRequest.toString());
            // TODO make suitable respone. add message.
            return RedisCommandExecuteResult.getEmptyResult("500");
        }

        if (commandRunner instanceof RedisCommandRunner) {
            logger.debug(commandRunner.getClass().getSimpleName() + " class found!");
        }
        else {
            throw new NotImplementedException("Connection pool not implemented yet " + this.redisServerDetector.getRedisInstanceType());
        }

        return executeCommand((RedisCommandRunner) commandRunner);
    }

    private RedisCommandExecuteResult executeCommand(RedisCommandRunner commandRunner) throws Exception {
        RedisCommandExecuteResult result = commandRunner.executeAndGet();

        logger.info("Command execute with redisRequest " + commandRunner.toString());

        return result;
    }
}
