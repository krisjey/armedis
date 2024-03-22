
package com.github.armedis.redis.command.config;

import java.util.HashMap;
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
    
    // TODO get인지 set인지 구분.
    @Override
    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
        logger.info(redisRequest.toString());

//     TODO  위로 올리던지. 서버 그룹별로 명령어를 던지던지 아니면 loopup에서 가져오던지.

        String key = this.redisRequest.getKey();
        String value = this.redisRequest.getValue();

        String result = null;

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

        // execute command to each nodes.
        for (RedisNode node : nodes) {
            // TODO FIXME do not create connection for every execute.
            try (StatefulRedisConnection<String, String> connection = new RedisConnector(node).connect();) {
                result = connection.sync().configSet(key, value);
            }
            catch (Exception e) {
                logger.error("Error command " + this.redisRequest.toString(), e);
            }
        }

        return RedisCommandExecuteResultFactory.buildRedisCommandExecuteResult(result);
    }

    private RedisClusterWideCommand getRedisClusterWideCommandMode(String subCommand) {
        return redisClusterWideCommandSet.get(subCommand);
    }

    private static final Map<String, RedisClusterWideCommand> redisClusterWideCommandSet = new HashMap<String, RedisClusterWideCommand>();
    static {
        // memory management
        redisClusterWideCommandSet.put("activedefrag", RedisClusterWideCommand.ALL); // no

        // server memory
        redisClusterWideCommandSet.put("maxmemory-policy", RedisClusterWideCommand.ALL); // volatile-lru, volatile-lfu, volatile-random, volatile-ttl, allkeys-lru, allkeys-lfu, allkeys-random, noeviction
        redisClusterWideCommandSet.put("maxmemory-samples", RedisClusterWideCommand.ALL); // "5"
        redisClusterWideCommandSet.put("maxmemory", RedisClusterWideCommand.ALL); // byte

        // clients
        redisClusterWideCommandSet.put("timeout", RedisClusterWideCommand.ALL); // "0"
        redisClusterWideCommandSet.put("maxclients", RedisClusterWideCommand.ALL); // "4064"

        // snapshot
        redisClusterWideCommandSet.put("save", RedisClusterWideCommand.ALL); // 3600 1 300 100 60 10000
        redisClusterWideCommandSet.put("appendonly", RedisClusterWideCommand.ALL); // "no"

        // lazyfree
        redisClusterWideCommandSet.put("lazyfree-lazy-eviction", RedisClusterWideCommand.ALL); // "no" recommend yes
        redisClusterWideCommandSet.put("lazyfree-lazy-expire", RedisClusterWideCommand.ALL); // "no" recommend yes
        redisClusterWideCommandSet.put("lazyfree-lazy-server-del", RedisClusterWideCommand.ALL); // "no" recommend yes
        redisClusterWideCommandSet.put("lazyfree-lazy-user-del", RedisClusterWideCommand.ALL); // "no" recommend yes
        redisClusterWideCommandSet.put("lazyfree-lazy-user-flush", RedisClusterWideCommand.ALL); // "no" recommend yes

        // time limit
        redisClusterWideCommandSet.put("lua-time-limit", RedisClusterWideCommand.ALL); // "5000"
        redisClusterWideCommandSet.put("cluster-node-timeout", RedisClusterWideCommand.ALL); // "15000"
        redisClusterWideCommandSet.put("slowlog-log-slower-than", RedisClusterWideCommand.ALL); // "10000"

        // logging
        redisClusterWideCommandSet.put("syslog-ident", RedisClusterWideCommand.ALL); // "redis"
        redisClusterWideCommandSet.put("loglevel", RedisClusterWideCommand.ALL); // "notice"
        redisClusterWideCommandSet.put("appendfsync", RedisClusterWideCommand.ALL); // "everysec"
    }
}
