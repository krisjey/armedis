
package com.github.armedis.redis;

import java.util.Set;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.redis.connection.RedisServerDetector;
import com.github.armedis.redis.connection.RedisServerInfo;

@Component
public class RedisServerInfoMaker {
    private final Logger logger = LoggerFactory.getLogger(RedisServerInfoMaker.class);

    private ArmedisConfiguration armedisConfiguration;

    @Autowired
    public RedisServerInfoMaker(ArmedisConfiguration armedisConfiguration) {
        this.armedisConfiguration = armedisConfiguration;
    }

    public RedisServerInfo detectRedisServer() {
        RedisServerDetector redisServerDetector = new RedisServerDetector(
                armedisConfiguration.getRedisSeedAddress());

        Set<RedisNode> redisNodes = null;
        try {
            redisNodes = redisServerDetector.lookupNodes();
        }
        catch (OperationNotSupportedException e) {
            logger.info("Does not support impl.");
        }

        return new RedisServerInfo(redisNodes, redisServerDetector.getRedisInstanceType());
    }
}
