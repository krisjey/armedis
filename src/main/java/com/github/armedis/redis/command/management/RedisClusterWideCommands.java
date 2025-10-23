/**
 * 
 */
package com.github.armedis.redis.command.management;

import java.util.HashMap;
import java.util.Map;

import com.github.armedis.redis.command.RedisClusterWideCommand;

/**
 * 
 */
public class RedisClusterWideCommands {
    private static final Map<String, RedisClusterWideCommand> redisClusterWideCommandSet = new HashMap<String, RedisClusterWideCommand>();
    static {
        // management/server
        redisClusterWideCommandSet.put("save", RedisClusterWideCommand.ALL);
        redisClusterWideCommandSet.put("bgsave", RedisClusterWideCommand.ALL);

        // memory management
        redisClusterWideCommandSet.put("activedefrag", RedisClusterWideCommand.ALL);

        // server memory
        redisClusterWideCommandSet.put("maxmemory", RedisClusterWideCommand.ALL);
        redisClusterWideCommandSet.put("maxmemory-policy", RedisClusterWideCommand.ALL);
        redisClusterWideCommandSet.put("maxmemory-samples", RedisClusterWideCommand.ALL);

        // clients
        redisClusterWideCommandSet.put("timeout", RedisClusterWideCommand.ALL);
        redisClusterWideCommandSet.put("maxclients", RedisClusterWideCommand.ALL);

        // snapshot
        redisClusterWideCommandSet.put("save", RedisClusterWideCommand.ALL);
        redisClusterWideCommandSet.put("appendonly", RedisClusterWideCommand.ALL);

        // lazyfree
        redisClusterWideCommandSet.put("lazyfree-lazy-eviction", RedisClusterWideCommand.ALL);
        redisClusterWideCommandSet.put("lazyfree-lazy-expire", RedisClusterWideCommand.ALL);
        redisClusterWideCommandSet.put("lazyfree-lazy-server-del", RedisClusterWideCommand.ALL);
        redisClusterWideCommandSet.put("lazyfree-lazy-user-del", RedisClusterWideCommand.ALL);
        redisClusterWideCommandSet.put("lazyfree-lazy-user-flush", RedisClusterWideCommand.ALL);

        // time limit
        redisClusterWideCommandSet.put("lua-time-limit", RedisClusterWideCommand.ALL);
        redisClusterWideCommandSet.put("slowlog-log-slower-than", RedisClusterWideCommand.ALL);

        // logging
        redisClusterWideCommandSet.put("loglevel", RedisClusterWideCommand.ALL);
        redisClusterWideCommandSet.put("appendfsync", RedisClusterWideCommand.ALL);
    }

    /**
     * @param subCommand
     * @return
     */
    public static RedisClusterWideCommand get(String subCommand) {
        return redisClusterWideCommandSet.get(subCommand);
    }
}
