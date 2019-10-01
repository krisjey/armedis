package com.github.armedis.redis.connection;

import java.util.Set;

import com.github.armedis.redis.RedisNode;

import io.lettuce.core.api.StatefulRedisConnection;

public interface RedisNodeLookup {

    Set<RedisNode> lookup(StatefulRedisConnection<String, String> redisSeedConnection);

}
