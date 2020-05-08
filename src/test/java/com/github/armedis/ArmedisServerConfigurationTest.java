
package com.github.armedis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.armedis.config.ArmedisConfiguration;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class ArmedisServerConfigurationTest {

    @Autowired
    private ArmedisConfiguration armedisConfiguration;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.setProperty("SERVICE_PORT", "8081");
    }

    @Test
    public void test() {
        assertThat(armedisConfiguration.getRedisSeedAddress()).isNotNull();
    }
}
