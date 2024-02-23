
package com.github.armedis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.armedis.config.ArmedisConfiguration;

@SpringBootTest
public class ArmedisServerConfigurationTest {

	@Autowired
	private ArmedisConfiguration armedisConfiguration;

	@BeforeAll
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
