package com.github.armedis.redis.connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
class RedisConfigTest extends AbstractRedisServerTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testRedisConnection() {
        redisTemplate.opsForValue().set("test-key",
                new ObjectMapper().createObjectNode().put("message", "Hello Redis").toString());

        String value = (String) redisTemplate.opsForValue().get("test-key");
        assertNotNull(value);
    }
}