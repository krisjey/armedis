package com.github.armedis.config.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("testbed")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
class RedisPropertiesTest extends AbstractRedisServerTest {

    @Autowired
    private RedisProperties redisProperties;

    @Test
    void testRedisPropertiesLoaded() {
        assertNotNull(redisProperties);
        assertEquals("192.168.56.105", redisProperties.getHost());
        assertEquals(17001, redisProperties.getPort());
    }

    @Test
    void testHasPassword() {
        // application-test.yml에서 password가 비어있으므로 false
        assertFalse(redisProperties.hasPassword());
    }

    @Test
    void testToString() {
        String str = redisProperties.toString();
        assertNotNull(str);
        assertTrue(str.contains("192.168.56.105"));
        assertTrue(str.contains("17001"));
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