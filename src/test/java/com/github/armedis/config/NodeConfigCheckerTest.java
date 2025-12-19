/**
 * 
 */
package com.github.armedis.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
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
public class NodeConfigCheckerTest extends AbstractRedisServerTest {
    @Autowired
    private NodeConfigChecker nodeConfigChecker;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisServerDetector redisServerDetector;

    @Test
    void testGetTimeout() {
        // slowlog-log-slower-than
        String oldValue = stringRedisTemplate.execute((RedisConnection connection) -> {
            // CONFIG SET <key> <value>
            Object raw = connection.execute("CONFIG", "GET".getBytes(), "slowlog-log-slower-than".getBytes());
            return raw == null ? null : raw.toString(); // 보통 "OK"
        });

        // 단일 노드가 아니면
        if (!redisServerDetector.getRedisInstanceType().equals(RedisInstanceType.STANDALONE)) {
            stringRedisTemplate.execute((RedisConnection connection) -> {
                // CONFIG SET <key> <value>
                Object raw = connection.execute("CONFIG", "SET".getBytes(), "slowlog-log-slower-than".getBytes(), "10000000".getBytes());
                return raw == null ? null : raw.toString(); // 보통 "OK"
            });
        }

        String slowlogTime = nodeConfigChecker.getConfigValue("slowlog-log-slower-than");

        for (int i = 0; i < 100; i++) {
            nodeConfigChecker.getConfigValue("slowlog-log-slower-than");
        }
        assertThat(slowlogTime).isNotNull();

        // 단일 노드가 아니면
        if (!redisServerDetector.getRedisInstanceType().equals(RedisInstanceType.STANDALONE)) {
            assertThat(slowlogTime).endsWith("(+)");
        }
        else {

        }
        System.out.println("------------" + slowlogTime + " ------------ " + oldValue);

        boolean setResult = nodeConfigChecker.setConfigValue("slowlog-log-slower-than", oldValue);
        assertThat(setResult).isTrue();
    }

    @Disabled
    @Test
    void testSetTimeout() {
        boolean result = nodeConfigChecker.setConfigValue("timeout", "0");

        for (int i = 0; i < 100; i++) {
            nodeConfigChecker.getConfigValue("timeout");
        }
        assertThat(result).isTrue();
    }
}
