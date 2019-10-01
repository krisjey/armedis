package com.github.armedis.redis.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.connection.pool.RedisConnectionPool;

import io.lettuce.core.api.StatefulRedisConnection;

@Component
public class RedisConnectionFactory implements RedisConnectionPool<String, String> {
    private final Logger logger = LoggerFactory.getLogger(RedisConnectionFactory.class);

    @Autowired
    RedisServerInfo redisConnectionInfo;
    
//    @Autowired
//    RedisConnectionPool redisConnectionPool;

    @Override
    public StatefulRedisConnection<String, String> getConnection() {
        logger.info(redisConnectionInfo.getRedisInstanceType().toString());
        logger.info(redisConnectionInfo.getRedisNodes().toString());
        return null;
    }

}
