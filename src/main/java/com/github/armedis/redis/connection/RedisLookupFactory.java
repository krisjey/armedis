package com.github.armedis.redis.connection;

import javax.naming.OperationNotSupportedException;

import com.github.armedis.redis.RedisClusterNodeLookup;
import com.github.armedis.redis.RedisInstanceType;

public class RedisLookupFactory {

    public static RedisNodeLookup create(RedisInstanceType type) throws OperationNotSupportedException {
        RedisNodeLookup lookup = null;

        switch (type) {
            case CLUSTER:
                lookup = new RedisClusterNodeLookup();
                break;

            case STANDALONE:
                lookup = new RedisStandaloneNodeLookup();
                break;

            case SENTINEL:
                lookup = new RedisSentinelNodeLookup();
                break;

            case NOT_DETECTED:
                throw new OperationNotSupportedException();

            default:
                throw new OperationNotSupportedException();
        }

        return lookup;
    }
}
