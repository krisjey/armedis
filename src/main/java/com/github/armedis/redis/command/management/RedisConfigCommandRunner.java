
package com.github.armedis.redis.command.management;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisClusterWideCommand;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RedisConfigRequest;
import com.github.armedis.redis.command.RequestRedisCommandName;
import com.github.armedis.redis.connection.RedisConnector;
import com.github.armedis.redis.connection.RedisServerDetector;

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
        String value = this.redisRequest.getValue();
        String result = commands.configSet(key, value);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

    // TODO sub command를 get인지 set인지 구분.
    @Override
    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
        String key = this.redisRequest.getKey();
        String value = this.redisRequest.getValue();

        Set<RedisNode> nodes = null;

        RedisClusterWideCommand mode = getRedisClusterWideCommandMode(key);
        switch (mode) {
            case MASTER:
                nodes = RedisServerDetector.getMasterNodes();

                break;

            case SLAVE:
                nodes = RedisServerDetector.getReplicaNodes();
                break;

            case ALL:
                nodes = RedisServerDetector.getAllNodes();

                break;

            default:
                logger.error("Can not execute command for cluster mode");
        }

        boolean executedFlag = false;
        String result = null;
        String prevValue = null;

        // execute command to each nodes.
        for (RedisNode node : nodes) {
            RedisConnector connector = new RedisConnector(node);
            try (StatefulRedisConnection<String, String> connection = connector.connect();) {
                // get or set config
                if (this.redisRequest.getRequestMethod().equalsIgnoreCase("GET")) {
                    Map<String, String> configResults = connection.sync().configGet(key);
                    String currValue = configResults.get(key);
                    if (executedFlag) {
                        if (prevValue.equals(currValue)) {
                            // do nothing.
                        }
                        else {
                            currValue = currValue + "(*)";
                        }
                    }

                    result = prevValue = currValue;
                }
                else { // config set
                    result = connection.sync().configSet(key, value);
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
                    logger.error("Connection close error for Config commands");
                }
            }

            executedFlag = true;
        }

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

    private RedisClusterWideCommand getRedisClusterWideCommandMode(String subCommand) {
        return RedisClusterWideCommands.get(subCommand);
    }

}
