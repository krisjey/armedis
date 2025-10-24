
package com.github.armedis.redis.command.management;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RequestRedisCommandName;
import com.github.armedis.redis.connection.RedisConnector;
import com.github.armedis.redis.connection.RedisServerDetector;
import com.linecorp.armeria.common.HttpMethod;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

@Component
@Scope("prototype")
@RequestRedisCommandName(RedisCommandEnum.CONFIG)
public class RedisConfigCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisConfigCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisConfigCommandRunner.class);

    private RedisConfigRequest redisRequest;

    public RedisConfigCommandRunner(RedisConfigRequest redisRequest) {
        this.redisRequest = redisRequest;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();

        if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(commands.configGet(key));
        }
        else {
            Optional<String> value = this.redisRequest.getValue();
            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(commands.configSet(key, value.get()));
        }
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
        logger.info(redisRequest.toString());

        String key = this.redisRequest.getKey();
        Optional<String> value = this.redisRequest.getValue();
        
        // TODO command validator

        Set<RedisNode> nodes = RedisServerDetector.getAllNodes();

        Map<String, String> getResult = null;
        String postResult = null;
        // execute command to each nodes.
        for (RedisNode node : nodes) {
            // TODO FIXME do not create connection for every execute.
            RedisConnector connector = new RedisConnector(node);
            try (StatefulRedisConnection<String, String> connection = connector.connect();) {
                if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
                    getResult = connection.sync().configGet(key);
                }
                else {
                    postResult = connection.sync().configSet(key, value.get());
                }
            }
            catch (Exception e) {
                logger.error("Error command " + this.redisRequest.toString(), e);
            }
            finally {
                try {
                    connector.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(getResult);
        }
        else {
            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(postResult);
        }
    }
}
