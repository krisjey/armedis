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
        redisClusterWideCommandSet.put("save", RedisClusterWideCommand.ALL); // no
        redisClusterWideCommandSet.put("bgsave", RedisClusterWideCommand.ALL); // no
        
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

    /**
     * @param subCommand
     * @return
     */
    public static RedisClusterWideCommand get(String subCommand) {
        return redisClusterWideCommandSet.get(subCommand);
    }
}
