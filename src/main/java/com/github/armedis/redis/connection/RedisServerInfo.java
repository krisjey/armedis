
package com.github.armedis.redis.connection;

import static java.util.Objects.requireNonNull;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisNode;

public class RedisServerInfo {
    private Set<RedisNode> redisNodes;
    private RedisInstanceType redisInstanceType;

    private final Logger logger = LoggerFactory.getLogger(RedisServerInfo.class);

    public RedisServerInfo(Set<RedisNode> redisNodes, RedisInstanceType redisInstanceType) {
        requireNonNull(redisNodes, "Redis server info not detected!");
        requireNonNull(redisInstanceType, "Redis server type not detected!");
        this.redisNodes = redisNodes;
        this.redisInstanceType = redisInstanceType;

        logger.info("RedisServerInfo created " + getRedisNodes());
    }

    public Set<RedisNode> getRedisNodes() {
        return redisNodes;
    }

    public RedisInstanceType getRedisInstanceType() {
        return redisInstanceType;
    }
}
