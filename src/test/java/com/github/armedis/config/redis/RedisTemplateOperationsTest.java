package com.github.armedis.config.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;

/**
 * RedisTemplate Operations 테스트
 * String, Hash, Key Operations 등을 검증
 */
@ActiveProfiles("testbed")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class RedisTemplateOperationsTest extends AbstractRedisServerTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_KEY_PREFIX = "test:";
    private String testKey;

    @BeforeEach
    public void setupTestKey() {
        testKey = TEST_KEY_PREFIX + System.currentTimeMillis();
    }

    @AfterEach
    public void cleanupTestKey() {
        if (testKey != null) {
            redisTemplate.delete(testKey);
        }
    }

    @Test
    public void testStringOperations_SetAndGet() {
        // Given
        ObjectNode jsonValue = objectMapper.createObjectNode();
        jsonValue.put("name", "John Doe");
        jsonValue.put("age", 30);

        // When
        redisTemplate.opsForValue().set(testKey, jsonValue.asText());
        String result = (String) redisTemplate.opsForValue().get(testKey);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(jsonValue.toString());

        System.out.println("SET/GET test passed: " + result);
    }

    @Test
    public void testStringOperations_SetWithExpire() throws InterruptedException {
        // Given
        ObjectNode jsonValue = objectMapper.createObjectNode();
        jsonValue.put("message", "This will expire");

        // When
        redisTemplate.opsForValue().set(testKey, jsonValue.toString(), 2, TimeUnit.SECONDS);

        // Then - 즉시 조회 시 존재해야 함
        String result = (String) redisTemplate.opsForValue().get(testKey);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(jsonValue.toString());

        // Wait for expiration
        Thread.sleep(3000);

        // Then - 만료 후 조회 시 null이어야 함
        String expiredResult = (String) redisTemplate.opsForValue().get(testKey);
        assertThat(expiredResult).isNull();

        System.out.println("SET with EXPIRE test passed");
    }

    @Test
    public void testStringOperations_SetIfAbsent() {
        // Given
        ObjectNode jsonValue1 = objectMapper.createObjectNode();
        jsonValue1.put("value", "first");

        ObjectNode jsonValue2 = objectMapper.createObjectNode();
        jsonValue2.put("value", "second");

        // When
        Boolean firstSet = redisTemplate.opsForValue().setIfAbsent(testKey, jsonValue1.toString());
        Boolean secondSet = redisTemplate.opsForValue().setIfAbsent(testKey, jsonValue2.toString());

        // Then
        assertThat(firstSet).isTrue();
        assertThat(secondSet).isFalse();

        String result = (String) redisTemplate.opsForValue().get(testKey);
        assertThat(result).isEqualTo(jsonValue2.toString());

        System.out.println("SETNX test passed");
    }

    @Test
    public void testHashOperations_PutAndGet() {
        // Given
        String hashKey = "user:1001";
        ObjectNode userData = objectMapper.createObjectNode();
        userData.put("email", "user@example.com");

        // When
        redisTemplate.opsForHash().put(testKey, hashKey, userData);
        JsonNode result = (JsonNode) redisTemplate.opsForHash().get(testKey, hashKey);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("email").asText()).isEqualTo("user@example.com");

        System.out.println("HSET/HGET test passed: " + result);
    }

    @Test
    public void testHashOperations_GetAllEntries() {
        // Given
        ObjectNode user1 = objectMapper.createObjectNode();
        user1.put("name", "Alice");

        ObjectNode user2 = objectMapper.createObjectNode();
        user2.put("name", "Bob");

        // When
        redisTemplate.opsForHash().put(testKey, "user:1", user1);
        redisTemplate.opsForHash().put(testKey, "user:2", user2);

        // Then
        assertThat(redisTemplate.opsForHash().size(testKey)).isEqualTo(2);
        assertThat(redisTemplate.opsForHash().hasKey(testKey, "user:1")).isTrue();
        assertThat(redisTemplate.opsForHash().hasKey(testKey, "user:2")).isTrue();

        System.out.println("HGETALL test passed, hash size: " + redisTemplate.opsForHash().size(testKey));
    }

    @Test
    public void testKeyOperations_Delete() {
        // Given
        ObjectNode jsonValue = objectMapper.createObjectNode();
        jsonValue.put("temp", "data");
        redisTemplate.opsForValue().set(testKey, jsonValue.toString());

        // When
        Boolean deleteResult = redisTemplate.delete(testKey);

        // Then
        assertThat(deleteResult).isTrue();
        assertThat(redisTemplate.hasKey(testKey)).isFalse();

        System.out.println("DELETE test passed");
    }

    @Test
    public void testKeyOperations_Expire() {
        // Given
        ObjectNode jsonValue = objectMapper.createObjectNode();
        jsonValue.put("data", "will expire");
        redisTemplate.opsForValue().set(testKey, jsonValue.toString());

        // When
        Boolean expireResult = redisTemplate.expire(testKey, 10, TimeUnit.SECONDS);

        // Then
        assertThat(expireResult).isTrue();
        Long ttl = redisTemplate.getExpire(testKey, TimeUnit.SECONDS);
        assertThat(ttl).isGreaterThan(0).isLessThanOrEqualTo(10);

        System.out.println("EXPIRE test passed, TTL: " + ttl + " seconds");
    }

    @Test
    public void testKeyOperations_HasKey() {
        // Given
        ObjectNode jsonValue = objectMapper.createObjectNode();
        jsonValue.put("exists", true);

        // When
        redisTemplate.opsForValue().set(testKey, jsonValue.toString());

        // Then
        assertThat(redisTemplate.hasKey(testKey)).isTrue();
        assertThat(redisTemplate.hasKey(testKey + ":nonexistent")).isFalse();

        System.out.println("EXISTS test passed");
    }
}