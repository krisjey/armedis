
package com.github.armedis.redis;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.redis.connection.RedisServerDetector;
import com.github.armedis.redis.connection.RedisServerInfo;

@Component
public class RedisServerInfoMaker {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ArmedisConfiguration armedisConfiguration;

    private RedisServerInfo redisServerInfo;

    private RedisServerDetector redisServerDetector;

    @Autowired
    public RedisServerInfoMaker(ArmedisConfiguration armedisConfiguration, RedisServerDetector redisServerDetector) {
        this.armedisConfiguration = armedisConfiguration;
        this.redisServerDetector = redisServerDetector;
    }

    public RedisServerInfo getRedisServerInfo() {
        if (this.redisServerInfo == null) {
            Set<RedisNode> redisNodes = null;
            try {
                redisNodes = redisServerDetector.getAllNodes();
            }
            catch (UnsupportedOperationException e) {
                logger.info("Does not support impl.");
            }

            RedisInstanceType redisInstanceType = redisServerDetector.getRedisInstanceType();

            redisServerInfo = new RedisServerInfo(redisNodes, redisInstanceType);
            redisInstanceType = redisServerInfo.getRedisInstanceType();
        }

        return redisServerInfo;
    }
}
