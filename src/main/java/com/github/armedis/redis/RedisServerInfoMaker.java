
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

    @Autowired
    public RedisServerInfoMaker(ArmedisConfiguration armedisConfiguration) {
        this.armedisConfiguration = armedisConfiguration;
    }

    public RedisServerInfo getRedisServerInfo() {
        if (this.redisServerInfo == null) {
            RedisServerDetector redisServerDetector = new RedisServerDetector(armedisConfiguration.getRedisSeedHost(),
                    armedisConfiguration.getRedisSeedPort());

            Set<RedisNode> redisNodes = null;
            try {
                redisNodes = redisServerDetector.lookupNodes();
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
