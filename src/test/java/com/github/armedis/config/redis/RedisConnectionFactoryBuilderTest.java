package com.github.armedis.config.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;
import com.github.armedis.redis.connection.RedisServerDetector;

/**
 * RedisConnectionFactoryBuilder 테스트
 * 각 Redis 서버 타입별 ConnectionFactory 생성 로직 검증
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class RedisConnectionFactoryBuilderTest extends AbstractRedisServerTest {
    @Autowired
    private RedisServerDetector redisServerDetector;

    @Test
    public void testConnectionFactoryBuilder() {
        // Given

        // When
        RedisConnectionFactory factory = new RedisConnectionFactoryBuilder(redisServerDetector).build();

        // Then
        assertThat(factory).isNotNull();
        assertThat(factory.getConnection()).isNotNull();
        assertThat(factory.getConnection().ping()).isNotNull();
        assertThat(factory.getConnection().ping()).isEqualTo("PONG");

        System.out.println("Standalone ConnectionFactory created successfully");
    }

    @Test
    public void testBuilderWithPoolConfig() {
        // When
        RedisConnectionFactory factory = new RedisConnectionFactoryBuilder(redisServerDetector)
                .withPoolConfig(10, 10, 5, java.time.Duration.ofMillis(3000), java.time.Duration.ofSeconds(30))
                .build();

        // Then
        assertThat(factory).isNotNull();
        assertThat(factory.getConnection()).isNotNull();

        System.out.println("ConnectionFactory with custom pool config created successfully");
    }
}