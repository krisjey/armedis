package com.github.armedis.redis.lookup;

import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

import com.github.armedis.redis.RedisInstance;
import com.github.armedis.redis.RedisNodeLookup;

import io.lettuce.core.api.StatefulRedisConnection;

public class RedisStandaloneNodeLookup implements RedisNodeLookup {

    @Override
    public Set<RedisInstance> lookup(StatefulRedisConnection<String, String> redisSeedConnection) {
        throw new NotImplementedException();
    }

}
