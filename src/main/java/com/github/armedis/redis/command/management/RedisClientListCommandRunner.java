
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
import com.github.armedis.redis.command.RedisClusterWideCommand;
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
public class RedisClientListCommandRunner extends AbstractRedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisClientListCommandRunner.class);

    @SuppressWarnings("unused")
    private static final boolean classLoaded = detectAnnotation(RedisClientListCommandRunner.class);

    private RedisSlowlogRequest redisRequest;

    public RedisClientListCommandRunner(RedisSlowlogRequest redisRequest) {
        this.redisRequest = redisRequest;
    }

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands) {
        logger.info(redisRequest.toString());
        Integer size = redisRequest.getSize().orElse(10);

        var result = commands.slowlogGet(size);

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result, Object.class);
    }

    // TODO sub command를 get인지 set인지 구분.
    @Override
    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
        logger.info(redisRequest.toString());
        Integer size = redisRequest.getSize().orElse(10);

        var result = commands.slowlogGet(size);
        
        System.out.println(commands.clientList());

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result, Object.class);

//        Set<RedisNode> nodes = null;
//
//        RedisClusterWideCommand mode = getRedisClusterWideCommandMode(key);
//        switch (mode) {
//            case MASTER:
//                nodes = RedisServerDetector.getMasterNodes();
//
//                break;
//
//            case SLAVE:
//                nodes = RedisServerDetector.getReplicaNodes();
//                break;
//
//            case ALL:
//                nodes = RedisServerDetector.getAllNodes();
//
//                break;
//
//            default:
//                logger.error("Can not execute command for cluster mode");
//        }
//
//        Map<String, String> getResult = null;
//        String setResult = null;
//        // execute command to each nodes.
//        for (RedisNode node : nodes) {
//            // TODO FIXME do not create connection for every execute.
//            RedisConnector connector = new RedisConnector(node);
//            try (StatefulRedisConnection<String, String> connection = connector.connect();) {
//                if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
//                    getResult = connection.sync().configGet(key);
//                }
//                else {
//                    setResult = connection.sync().configSet(key, value.get());
//                }
//            }
//            catch (Exception e) {
//                logger.error("Error command " + this.redisRequest.toString(), e);
//            }
//            finally {
//                try {
//                    connector.close();
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        if (redisRequest.getRequestMethod().equals(HttpMethod.GET)) {
//            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(getResult);
//        }
//        else {
//            return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(setResult);
//        }
    }
}
