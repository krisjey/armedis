
package com.github.armedis.redis.connection;

import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;

import com.github.armedis.redis.RedisNode;

import io.lettuce.core.api.StatefulRedisConnection;

public class RedisSentinelNodeLookup implements RedisNodeLookup {

    @Override
    public Set<RedisNode> lookup(StatefulRedisConnection<String, String> redisSeedConnection) {
        throw new NotImplementedException("Currently not implemented yet.");
    }

}
