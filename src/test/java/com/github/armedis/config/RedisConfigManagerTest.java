/**
 * 
 */
package com.github.armedis.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;
import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.connection.RedisServerDetector;

/**
 * 
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class RedisConfigManagerTest extends AbstractRedisServerTest {
    @Autowired
    private RedisConfigManager redisConfigManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisServerDetector redisServerDetector;

    @Test
    void testGetTimeout() {
        // slowlog-log-slower-than
        Object oldValue = stringRedisTemplate.execute((RedisConnection connection) -> {
            Properties props = connection.serverCommands().getConfig("slowlog-log-slower-than");
            String key = (String) props.stringPropertyNames().toArray()[0];
            return props.get(key);
        });
        
        String value = (oldValue == null) ? null : String.valueOf(oldValue);
//        long slowlogLogSlowerThan = (value == null) ? -1L : Long.parseLong(value);

        // 단일 노드가 아니면
        if (!redisServerDetector.getRedisInstanceType().equals(RedisInstanceType.STANDALONE)) {
            stringRedisTemplate.execute((RedisConnection connection) -> {
                // CONFIG SET <key> <value>
                Object raw = connection.execute("CONFIG", "SET".getBytes(), "slowlog-log-slower-than".getBytes(), "10000000".getBytes());
                return raw == null ? null : raw.toString(); // 보통 "OK"
            });
        }

        String slowlogTime = redisConfigManager.getConfigValue("slowlog-log-slower-than");

        for (int i = 0; i < 100; i++) {
            redisConfigManager.getConfigValue("slowlog-log-slower-than");
        }
        assertThat(slowlogTime).isNotNull();

        // 단일 노드가 아니면
        if (!redisServerDetector.getRedisInstanceType().equals(RedisInstanceType.STANDALONE)) {
            assertThat(slowlogTime).endsWith("(+)");
        }
        else {

        }
        System.out.println("------------" + slowlogTime + " ------------ " + value);

        boolean setResult = redisConfigManager.setConfigValue("slowlog-log-slower-than", value);
        assertThat(setResult).isTrue();
    }

    @Test
    void testSetTimeout() {
        boolean result = redisConfigManager.setConfigValue("timeout", "0");

        for (int i = 0; i < 100; i++) {
            redisConfigManager.getConfigValue("timeout");
        }
        assertThat(result).isTrue();
    }
}
