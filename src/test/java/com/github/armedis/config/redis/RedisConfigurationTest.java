package com.github.armedis.config.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.github.armedis.ArmedisServer;
import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.http.service.AbstractRedisServerTest;

/**
 * RedisConfiguration 테스트
 * Redis Template 및 ConnectionFactory가 정상적으로 생성되는지 검증
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class RedisConfigurationTest extends AbstractRedisServerTest {

    @Autowired
    private ArmedisConfiguration armedisConfiguration;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisPropertiesLoaded() {
        // Redis Properties가 정상적으로 로드되었는지 확인
        assertThat(armedisConfiguration).isNotNull();
        assertThat(armedisConfiguration.getRedisSeedHost()).isNotNull();
        assertThat(armedisConfiguration.getRedisSeedPort()).isGreaterThan(0);

        System.out.println("Redis Properties: " + armedisConfiguration);
    }

    @Test
    public void testRedisConnectionFactoryCreated() {
        // RedisConnectionFactory가 정상적으로 생성되었는지 확인
        assertThat(redisConnectionFactory).isNotNull();

        // Connection 테스트
        assertThat(redisConnectionFactory.getConnection()).isNotNull();

        System.out.println("RedisConnectionFactory: " + redisConnectionFactory.getClass().getSimpleName());
    }

    @Test
    public void testRedisTemplateCreated() {
        // RedisTemplate이 정상적으로 생성되었는지 확인
        assertThat(redisTemplate).isNotNull();
        assertThat(redisTemplate.getConnectionFactory()).isNotNull();

        // Serializer 확인
        assertThat(redisTemplate.getKeySerializer()).isNotNull();
        assertThat(redisTemplate.getValueSerializer()).isNotNull();
        assertThat(redisTemplate.getHashKeySerializer()).isNotNull();
        assertThat(redisTemplate.getHashValueSerializer()).isNotNull();

        System.out.println("RedisTemplate created successfully");
    }

    @Test
    public void testRedisConnectionFactoryConnection() {
        // Connection이 실제로 동작하는지 확인
        assertThat(redisConnectionFactory.getConnection().ping()).isNotNull();

        System.out.println("Redis Connection PING successful");
    }
}