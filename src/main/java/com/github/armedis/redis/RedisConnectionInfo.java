package com.github.armedis.redis;

import static java.util.Objects.requireNonNull;

import java.util.Set;

public class RedisConnectionInfo {
    private Set<RedisInstance> redisNodes;
    private RedisInstanceType redisInstanceType;

    public RedisConnectionInfo(Set<RedisInstance> redisNodes, RedisInstanceType redisInstanceType) {
        requireNonNull(redisNodes, "Redis server info not detected!");
        requireNonNull(redisInstanceType, "Redis server type not detected!");
        this.redisNodes = redisNodes;
        this.redisInstanceType = redisInstanceType;
    }

    public Set<RedisInstance> getRedisNodes() {
        return redisNodes;
    }

    public RedisInstanceType getRedisInstanceType() {
        return redisInstanceType;
    }

}
