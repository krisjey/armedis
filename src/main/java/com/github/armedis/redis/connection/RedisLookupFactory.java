
package com.github.armedis.redis.connection;

import com.github.armedis.redis.RedisInstanceType;

public class RedisLookupFactory {

    public static RedisNodeLookup create(RedisInstanceType type, String seedAddresses) throws UnsupportedOperationException {
        RedisNodeLookup lookup = null;

        switch (type) {
            case CLUSTER:
                lookup = new RedisClusterNodeLookup();
                break;

            case STANDALONE:
                lookup = new RedisNoneClusterNodeLookup(seedAddresses);
                break;

            case SENTINEL:
                lookup = new RedisSentinelNodeLookup();
                break;

            case NOT_DETECTED:
                throw new UnsupportedOperationException();

            default:
                throw new UnsupportedOperationException();
        }

        return lookup;
    }
}
