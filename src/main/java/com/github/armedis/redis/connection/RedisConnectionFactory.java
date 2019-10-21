
package com.github.armedis.redis.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.connection.pool.RedisConnectionPool;

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;

@Component
public class RedisConnectionFactory {

    @Autowired
    private RedisConnectionPool<String, String> redisConnectionPool;

    public StatefulRedisClusterConnection<String, String> getConnection() throws Exception {
//        logger.info(redisConnectionInfo.getRedisInstanceType().toString());
//        logger.info(redisConnectionInfo.getRedisNodes().toString());

//        RedisClusterClient redisClient = RedisClusterClient.create("redis://password@localhost:7379");
//        StatefulRedisClusterConnection<String, String> con = redisClient.connect();

//        RedisCommand command = con.sync();
//        AdvancedClusterCommands<String, String> command = con.sync();

//        redisClient.connect();

        return redisConnectionPool.getConnection();
    }

}
