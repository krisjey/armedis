
package com.github.armedis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.armedis.config.ArmedisConfiguration;

// if use junit4 then just use RunWith annotation.
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArmedisServerConfigurationTest {

    @Autowired
    private ArmedisConfiguration armedisConfiguration;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Random rnd = new Random();
        Integer portNumber = rnd.nextInt(1000) + 8001;
        System.setProperty("SERVICE_PORT", String.valueOf(portNumber));
    }

    @Test
    public void test() {
        assertThat(armedisConfiguration.getRedisSeedAddress()).isNotNull();
    }
}
