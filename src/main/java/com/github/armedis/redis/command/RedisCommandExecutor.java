
package com.github.armedis.redis.command;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisServerInfoMaker;

@Component
public class RedisCommandExecutor {
    private final Logger logger = LoggerFactory.getLogger(RedisCommandExecutor.class);

//    private RedisConnectionPool<String, String> redisConnectionPool;
    private RedisTemplate<String, Object> redisTemplate;

    // TODO 서버 분기 제거, 분기 필요 없음.
    private RedisInstanceType redisServerInfo;

    private BeanFactory beanFactory;

    @Autowired
    public RedisCommandExecutor(RedisTemplate<String, Object> redisTemplate, RedisServerInfoMaker redisServerInfoMaker, BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.redisTemplate = redisTemplate;
        this.redisServerInfo = redisServerInfoMaker.getRedisServerInfo().getRedisInstanceType();
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
            // do nothing.
            logger.debug(commandRunner.getClass().getSimpleName() + " class found!");
        }
        else {
            throw new NotImplementedException("Connection pool not implemented yet " + redisServerInfo.toString());
        }

        switch (this.redisServerInfo) {
            case STANDALONE:
                // Bean lookup and execute on cluster server.
                return executeNonClusterCommand((RedisCommandRunner) commandRunner);

            case SENTINEL:
                // Bean lookup and execute on cluster server.
                return executeNonClusterCommand((RedisCommandRunner) commandRunner);

            case CLUSTER:
                // Bean lookup and execute on cluster server.
                return executeClusterCommand((RedisCommandRunner) commandRunner);

            default:
                throw new NotImplementedException("Connection pool not implemented yet " + redisServerInfo.toString());
        }
    }

    private RedisCommandExecuteResult executeNonClusterCommand(RedisCommandRunner commandRunner) throws Exception {
//        StatefulRedisConnection<String, String> connection = this.redisConnectionPool.getNonClusterConnection();
//        RedisCommands<String, String> commands = connection.sync();
//
//        RedisCommandExecuteResult result = commandRunner.executeAndGet(commands);
//
//        this.redisConnectionPool.returnObject(connection);

        RedisCommandExecuteResult result = commandRunner.executeAndGet();

        logger.info("Command execute with redisRequest " + commandRunner.toString());

        return result;
    }

    private RedisCommandExecuteResult executeClusterCommand(RedisCommandRunner commandRunner) throws Exception {
        // cluster is not null
        // send all cluster
        // send master
        // send slave

//        StatefulRedisClusterConnection<String, String> connection = this.redisConnectionPool.getClusterConnection();
//
//        RedisAdvancedClusterCommands<String, String> commands = connection.sync();
//
//        RedisCommandExecuteResult result = commandRunner.executeAndGet(commands);
//
//        this.redisConnectionPool.returnObject(connection);

        RedisCommandExecuteResult result = commandRunner.executeAndGet();

        logger.info("Command execute with redisRequest " + commandRunner.toString());

        return result;
    }
}
