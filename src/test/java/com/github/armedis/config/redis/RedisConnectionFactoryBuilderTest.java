package com.github.armedis.config.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.AbstractRedisServerTest;
import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisNode;

/**
 * RedisConnectionFactoryBuilder 테스트
 * 각 Redis 서버 타입별 ConnectionFactory 생성 로직 검증
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class RedisConnectionFactoryBuilderTest extends AbstractRedisServerTest {

    @Autowired
    private RedisProperties redisProperties;

    @Test
    public void testStandaloneConnectionFactoryBuilder() {
        // Given
        Set<RedisNode> nodes = new HashSet<>();
        nodes.add(new RedisNode(redisProperties.getHost(), redisProperties.getPort()));

        // When
        RedisConnectionFactory factory = new RedisConnectionFactoryBuilder(
                redisProperties,
                RedisInstanceType.STANDALONE,
                nodes).build();

        // Then
        assertThat(factory).isNotNull();
        assertThat(factory.getConnection()).isNotNull();
        assertThat(factory.getConnection().ping()).isNotNull();

        System.out.println("Standalone ConnectionFactory created successfully");
    }

    @Test
    public void testBuilderWithInvalidInstanceType() {
        // Given
        Set<RedisNode> nodes = new HashSet<>();
        nodes.add(new RedisNode(redisProperties.getHost(), redisProperties.getPort()));

        // When & Then
        assertThatThrownBy(() -> {
            new RedisConnectionFactoryBuilder(
                    redisProperties,
                    RedisInstanceType.NOT_DETECTED,
                    nodes).build();
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unsupported Redis instance type");

        System.out.println("Invalid instance type test passed");
    }

    @Test
    public void testBuilderWithPoolConfig() {
        // Given
        Set<RedisNode> nodes = new HashSet<>();
        nodes.add(new RedisNode(redisProperties.getHost(), redisProperties.getPort()));

        // When
        RedisConnectionFactory factory = new RedisConnectionFactoryBuilder(
                redisProperties,
                RedisInstanceType.STANDALONE,
                nodes)
                        .withPoolConfig(10, 10, 5, java.time.Duration.ofMillis(3000), java.time.Duration.ofSeconds(30))
                        .build();

        // Then
        assertThat(factory).isNotNull();
        assertThat(factory.getConnection()).isNotNull();

        System.out.println("ConnectionFactory with custom pool config created successfully");
    }
}