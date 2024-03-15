
package com.github.armedis.redis.command.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.command.AbstractRedisCommandRunner;
import com.github.armedis.redis.command.RedisClusterWideCommand;
import com.github.armedis.redis.command.RedisCommandEnum;
import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandExecuteResultFactory;
import com.github.armedis.redis.command.RedisConfigRequest;
import com.github.armedis.redis.command.RequestRedisCommandName;

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

    @Override
    public RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands) {
        logger.info(redisRequest.toString());
        
//     TODO  위로 올리던지. 서버 그룹별로 명령어를 던지던지

        String key = this.redisRequest.getKey();
        String value = this.redisRequest.getValue();

        RedisClusterWideCommand mode = getRedisClusterWideCommandMode(key);
        switch (mode) {
            case MASTER:

                break;

            case SLAVE:

                break;

            case ALL:

                break;

            default:
                logger.error("Can not execute command for cluster mode");
        }

        String result = commands.configSet(key, value);

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
        redisClusterWideCommandSet.put("maxmemory-policy", RedisClusterWideCommand.MASTER); // noeviction
        redisClusterWideCommandSet.put("maxmemory-samples", RedisClusterWideCommand.ALL); // "5"
        redisClusterWideCommandSet.put("maxmemory", RedisClusterWideCommand.SLAVE); // "0"

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
