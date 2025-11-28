/**
 * 
 */
package com.github.armedis.redis.connection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.armedis.config.ArmedisConfiguration;

/**
 * 
 */
@Configuration
public class RedisServerDetectorConfig {
    ArmedisConfiguration armedisConfiguration;

    public RedisServerDetectorConfig(ArmedisConfiguration armedisConfiguration) {
        this.armedisConfiguration = armedisConfiguration;
    }

    @Bean
    public RedisServerDetector redisServerDetector() {
        return new RedisServerDetector(armedisConfiguration.getRedisSeedHost(), armedisConfiguration.getRedisSeedPort(), armedisConfiguration.getRedisSeedPassword());
    }
}
