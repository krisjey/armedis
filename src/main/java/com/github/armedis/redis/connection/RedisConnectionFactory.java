
package com.github.armedis.redis.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.connection.pool.RedisConnectionPool;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.protocol.RedisCommand;

@Component
public class RedisConnectionFactory implements RedisConnectionPool<String, String> {
    private final Logger logger = LoggerFactory.getLogger(RedisConnectionFactory.class);

//    @Autowired
//    RedisConnectionPool<String, String> redisConnectionPool;

    @Override
    public StatefulRedisConnection<String, String> getConnection() {
//        logger.info(redisConnectionInfo.getRedisInstanceType().toString());
//        logger.info(redisConnectionInfo.getRedisNodes().toString());

//        RedisClusterClient redisClient = RedisClusterClient.create("redis://password@localhost:7379");
//        StatefulRedisClusterConnection<String, String> con = redisClient.connect();
        
//        RedisCommand command = con.sync();
//        AdvancedClusterCommands<String, String> command = con.sync();
        
//        redisClient.connect();

        return null;
    }

}
