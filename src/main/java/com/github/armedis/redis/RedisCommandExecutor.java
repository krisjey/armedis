
package com.github.armedis.redis;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.armedis.http.service.request.RedisRequest;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.connection.pool.RedisConnectionPool;
import com.github.armedis.spring.ApplicationContextProvider;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

@Component
public class RedisCommandExecutor {
    private final Logger logger = LoggerFactory.getLogger(RedisCommandExecutor.class);

    private final ObjectMapper mapper = new ObjectMapper();

    private RedisConnectionPool<String, String> redisConnectionPool;

    private RedisInstanceType redisServerInfo;

    private ApplicationContext context;

    @Autowired
    public RedisCommandExecutor(RedisConnectionPool<String, String> redisConnectionPool, RedisServerInfoMaker redisServerInfoMaker) {
        this.context = ApplicationContextProvider.getApplicationContext();
        this.redisConnectionPool = redisConnectionPool;
        this.redisServerInfo = redisServerInfoMaker.getRedisServerInfo().getRedisInstanceType();
    }

    public RedisCommandExecuteResult execute(RedisRequest redisRequest) throws Exception {
        String beanName = redisRequest.getCommand() + "RedisCommandRunner";

        Object commandRunner = this.context.getBean(beanName, redisRequest);

        // never enter the condition, There is no constructor with redisRequest class.
        if (commandRunner == null) {
            logger.warn("Can not found request bean name [" + beanName + "] " + redisRequest.toString());
            return RedisCommandExecuteResult.getEmptyResult();
        }

        if (commandRunner instanceof RedisCommandRunner) {
            // do nothing.
        }
        else {
            throw new NotImplementedException("Connection pool not implemented " + redisServerInfo.toString());
        }

        switch (this.redisServerInfo) {
            case STANDALONE:
            case SENTINEL:
                // 요청을 실행할 응답처리 Bean lookup.
                return executeNonClusterCommand((RedisCommandRunner) commandRunner);

            case CLUSTER:
                // 요청을 실행할 응답처리 Bean lookup.
                return executeClusterCommand((RedisCommandRunner) commandRunner);

            default:
                throw new NotImplementedException("Connection pool not implemented " + redisServerInfo.toString());
        }
    }

    private RedisCommandExecuteResult executeNonClusterCommand(RedisCommandRunner commandRunner) throws Exception {
        StatefulRedisConnection<String, String> connection = this.redisConnectionPool.getNonClusterConnection();
        RedisCommands<String, String> commands = connection.sync();

        RedisCommandExecuteResult result = commandRunner.run(commands);

//        JsonNode result = mapper.valueToTree(receivedValue);

        logger.info("Command execute with redisRequest " + commandRunner.toString());

        return result;
    }

    private RedisCommandExecuteResult executeClusterCommand(RedisCommandRunner commandRunner) throws Exception {
        StatefulRedisClusterConnection<String, String> connection = this.redisConnectionPool.getClusterConnection();
        RedisAdvancedClusterCommands<String, String> commands = connection.sync();

        commands.decr("");

        RedisCommandExecuteResult result = commandRunner.run(commands);

//        JsonNode result = mapper.valueToTree(receivedValue);

        logger.info("Command execute with redisRequest " + commandRunner.toString());

        return result;
    }
}
