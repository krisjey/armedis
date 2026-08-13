
package com.github.armedis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.armedis.config.ArmedisConfiguration;
import com.github.armedis.http.service.AbstractRedisServerTest;
import com.linecorp.armeria.common.ServerCacheControl;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class ArmedisServerConfigurationTest extends AbstractRedisServerTest {

    @Autowired
    private ArmedisConfiguration armedisConfiguration;

    @Test
    public void test() {
        assertThat(armedisConfiguration.getRedisSeedHost()).isNotNull();
        assertThat(armedisConfiguration.getRedisSeedPort()).isGreaterThan(0);
    }

    @Test
    void staticFilesRequireCacheRevalidation() {
        ServerCacheControl cacheControl = ArmedisServerConfiguration.staticFileCacheControl();

        assertThat(cacheControl.noCache()).isTrue();
        assertThat(cacheControl.mustRevalidate()).isTrue();
        assertThat(cacheControl.maxAgeSeconds()).isEqualTo(-1);
        assertThat(cacheControl.cachePublic()).isFalse();
    }
}
