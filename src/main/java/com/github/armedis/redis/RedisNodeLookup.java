package com.github.armedis.redis;

import java.util.Set;

import io.lettuce.core.api.StatefulRedisConnection;

public interface RedisNodeLookup {

    Set<RedisInstance> lookup(StatefulRedisConnection<String, String> redisSeedConnection);

}
