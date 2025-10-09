
package com.github.armedis.redis.command.management;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RequestRedisCommandName;
import com.github.armedis.redis.command.management.vo.ConnectedClient;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

@Component
@Scope("prototype")
@RequestRedisCommandName(RedisCommandEnum.CLIENT)
public class RedisClientListCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisClientListCommandRunner.class);
    private static final Integer LIMIT = 10;

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisClientListCommandRunner.class);

    private RedisClientListRequest redisRequest;

    public RedisClientListCommandRunner(RedisClientListRequest redisRequest) {
        this.redisRequest = redisRequest;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
        logger.info(redisRequest.toString());

        // TODO command send to all cluster
        String clientList = commands.clientList();

        List<ConnectedClient> result = ConnectedClientParser.parseClientList(clientList);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result.subList(0, LIMIT), Object.class);
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
        logger.info(redisRequest.toString());

        // TODO command send to all cluster
        String clientList = commands.clientList();

        List<ConnectedClient> result = ConnectedClientParser.parseClientList(clientList);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result, Object.class);
    }
}
