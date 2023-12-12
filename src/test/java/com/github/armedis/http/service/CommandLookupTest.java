
package com.github.armedis.http.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import com.github.armedis.redis.command.RedisCommandRunner;

@SpringBootTest
public class CommandLookupTest {
    @Autowired
    private ApplicationContext springContext;

    @BeforeTestClass
    public static void setUpBeforeClass() throws Exception {
        Random rnd = new Random();
        Integer portNumber = rnd.nextInt(1000) + 8001;
        System.setProperty("SERVICE_PORT", String.valueOf(portNumber));
    }

    @Test
    public void test() {
        String[] redisCommands = springContext.getBeanNamesForType(RedisCommandRunner.class);

        for (String redisCommand : redisCommands) {
            assertThat(redisCommand).isNotNull();
            assertThat(redisCommand).endsWith("CommandRunner");
        }
    }

}
