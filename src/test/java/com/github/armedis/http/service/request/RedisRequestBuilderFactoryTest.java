
package com.github.armedis.http.service.request;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.armedis.redis.command.RedisCommandEnum;

class RedisRequestBuilderFactoryTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

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

}
