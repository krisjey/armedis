package com.github.armedis.redis.connection;

import static java.util.Objects.requireNonNull;

import java.util.Set;

import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.RedisInstanceType;

public class RedisServerInfo {
    private Set<RedisNode> redisNodes;
    private RedisInstanceType redisInstanceType;

    public RedisServerInfo(Set<RedisNode> redisNodes, RedisInstanceType redisInstanceType) {
        requireNonNull(redisNodes, "Redis server info not detected!");
        requireNonNull(redisInstanceType, "Redis server type not detected!");
        this.redisNodes = redisNodes;
        this.redisInstanceType = redisInstanceType;
    }

    public Set<RedisNode> getRedisNodes() {
        return redisNodes;
    }

    public RedisInstanceType getRedisInstanceType() {
        return redisInstanceType;
    }

}
