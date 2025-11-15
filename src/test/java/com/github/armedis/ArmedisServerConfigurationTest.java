
package com.github.armedis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.http.service.AbstractRedisServerTest;

@ActiveProfiles("testbed")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class ArmedisServerConfigurationTest extends AbstractRedisServerTest {

    @Autowired
    private ArmedisConfiguration armedisConfiguration;

    @Test
    public void test() {
        assertThat(armedisConfiguration.getRedisSeedHost()).isNotNull();
        assertThat(armedisConfiguration.getRedisSeedPort()).isGreaterThan(0);
    }
}
