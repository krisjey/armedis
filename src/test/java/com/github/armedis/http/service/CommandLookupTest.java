
package com.github.armedis.http.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;

import com.github.armedis.ArmedisServer;
import com.github.armedis.redis.command.RedisCommandRunner;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class CommandLookupTest extends AbstractRedisServerTest {
    @Autowired
    private ApplicationContext springContext;

    @Test
    void test() {
        String[] redisCommands = springContext.getBeanNamesForType(RedisCommandRunner.class);

        for (String redisCommand : redisCommands) {
            assertThat(redisCommand).isNotNull();
            assertThat(redisCommand).endsWith("CommandRunner");
        }
    }
}
