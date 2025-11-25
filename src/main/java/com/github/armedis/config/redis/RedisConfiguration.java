package com.github.armedis.config.redis;

import java.time.Duration;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.RedisServerInfoMaker;
import com.github.armedis.redis.connection.RedisServerDetector;
import com.github.armedis.redis.connection.RedisServerInfo;

/**
 * Redis Template 설정
 * Seed 정보 기반으로 Redis 서버 타입을 감지하고 적절한 ConnectionFactory 및 Template 생성
 */
@Configuration
public class RedisConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

    private final ArmedisConfiguration armedisConfiguration;
    private final RedisServerInfoMaker redisServerInfoMaker;

    // Lettuce Pool 설정 (application.yml에서 주입)
    @Value("${spring.data.redis.lettuce.pool.max-active:8}")
    private int maxActive;

    @Value("${spring.data.redis.lettuce.pool.max-idle:8}")
    private int maxIdle;

    @Value("${spring.data.redis.lettuce.pool.min-idle:2}")
    private int minIdle;

    @Value("${spring.data.redis.lettuce.pool.max-wait:2000ms}")
    private Duration maxWait;

    @Value("${spring.data.redis.lettuce.pool.time-between-eviction-runs:60s}")
    private Duration timeBetweenEvictionRuns;

    public RedisConfiguration(ArmedisConfiguration armedisConfiguration,
            RedisServerInfoMaker redisServerInfoMaker) {
        this.armedisConfiguration = armedisConfiguration;
        this.redisServerInfoMaker = redisServerInfoMaker;
    }

    @Bean
    public RedisServerDetector redisServerDetector() {
        // 실제 seed host/port 는 yml 에서 읽어오거나 @Value 로 주입
        return new RedisServerDetector(armedisConfiguration.getRedisSeedHost(), armedisConfiguration.getRedisSeedPort(), armedisConfiguration.getRedisSeedPassword());
    }

    /**
     * RedisConnectionFactory Bean 생성
     * Seed 기반으로 Redis 서버 타입을 감지하고 적절한 Factory 생성
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        logger.info("Initializing RedisConnectionFactory with seed: {}", armedisConfiguration);

        try {
            // Redis 서버 정보 감지
            RedisServerInfo serverInfo = redisServerInfoMaker.getRedisServerInfo();
            RedisInstanceType instanceType = serverInfo.getRedisInstanceType();
            Set<RedisNode> redisNodes = serverInfo.getRedisNodes();

            logger.info("Detected Redis instance type: {}", instanceType);
            logger.info("Detected Redis nodes: {}", redisNodes.size());

            // ConnectionFactory 빌드
            RedisConnectionFactory factory = new RedisConnectionFactoryBuilder(redisServerDetector())
                    .withPoolConfig(maxActive, maxIdle, minIdle, maxWait, timeBetweenEvictionRuns)
                    .build();

            logger.info("RedisConnectionFactory created successfully for type: {}", instanceType);
            return factory;

        }
        catch (Exception e) {
            logger.error("Failed to create RedisConnectionFactory. Application will not start.", e);
            throw new IllegalStateException("Cannot connect to Redis seed server: " + armedisConfiguration, e);
        }
    }

    /**
     * RedisTemplate<String, Object> Bean 생성
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        logger.info("Creating RedisTemplate<String, Object>");

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key Serializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

//        // Value Serializer (Jackson2JsonRedisSerializer for JsonNode)
//        Jackson2JsonRedisSerializer<JsonNode> jsonSerializer = new Jackson2JsonRedisSerializer<>(new ObjectMapper(), JsonNode.class);
//
//        template.setValueSerializer(jsonSerializer);
//        template.setHashValueSerializer(jsonSerializer);

        template.setValueSerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        template.afterPropertiesSet();

        logger.info("RedisTemplate created successfully");
        return template;
    }
}