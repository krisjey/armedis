package com.github.armedis.config.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
class RedisPropertiesTest extends AbstractRedisServerTest {

    @Autowired
    private RedisProperties redisProperties;

    @Test
    void testRedisPropertiesLoaded() {
        assertNotNull(redisProperties);
        assertThat(redisProperties.getHost()).isNotNull();

        assertThat(redisProperties.getPort()).isGreaterThan(0);
    }

    @Test
    void testPasswordMasking() {
        RedisProperties props = new RedisProperties();
        props.setHost("testhost");
        props.setPort(1234);
        props.setPassword("secret123");

        String str = props.toString();
        assertFalse(str.contains("secret123"));
        assertTrue(str.contains("***"));
    }
}