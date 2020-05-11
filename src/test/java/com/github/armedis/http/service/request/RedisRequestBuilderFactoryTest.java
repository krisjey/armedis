
package com.github.armedis.http.service.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
 
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.armedis.redis.command.RedisCommandEnum;

class RedisRequestBuilderFactoryTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Random rnd = new Random();
        Integer portNumber = rnd.nextInt(1000) + 8001;
        System.setProperty("SERVICE_PORT", String.valueOf(portNumber));
    }

    @Disabled
    @Test
    public void testAllRequestBuilder() {
        for (RedisCommandEnum redisCommand : RedisCommandEnum.values()) {
            if (RedisCommandEnum.NOT_DETECTED == redisCommand) {
                continue;
            }

            RedisRequestBuilder builder = RedisRequestBuilderFactory.createRedisRequestBuilder(redisCommand.getCommand());
            assertThat(builder).as(redisCommand.getCommand()).isNotNull();
        }
    }

    @Test
    public void testImplementedRequestBuilder() {
        Set<RedisCommandEnum> implementedRequestBuilder = new HashSet<RedisCommandEnum>();
        implementedRequestBuilder.add(RedisCommandEnum.GET);
        implementedRequestBuilder.add(RedisCommandEnum.SET);

        for (RedisCommandEnum redisCommand : RedisCommandEnum.values()) {
            if (RedisCommandEnum.NOT_DETECTED == redisCommand) {
                continue;
            }

            if (implementedRequestBuilder.contains(redisCommand)) {
                RedisRequestBuilder builder = RedisRequestBuilderFactory.createRedisRequestBuilder(redisCommand.getCommand());
                assertThat(builder).as(redisCommand.getCommand()).isNotNull();
            }
        }
    }
}
