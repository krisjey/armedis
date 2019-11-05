
package com.github.armedis.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

public class RedisInstanceTypeTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() {
        RedisInstanceType type = RedisInstanceType.of("cluster");
        assertThat(type).isNotNull();
        assertThat(type).isEqualTo(RedisInstanceType.CLUSTER);

        type = RedisInstanceType.of("sentinel");
        assertThat(type).isNotNull();
        assertThat(type).isEqualTo(RedisInstanceType.SENTINEL);

        type = RedisInstanceType.of("standalone");
        assertThat(type).isNotNull();
        assertThat(type).isEqualTo(RedisInstanceType.STANDALONE);

        type = RedisInstanceType.of("");
        assertThat(type).isNotNull();
        assertThat(type).isEqualTo(RedisInstanceType.NOT_DETECTED);

        type = RedisInstanceType.of("null");
        assertThat(type).isNotNull();
        assertThat(type).isEqualTo(RedisInstanceType.NOT_DETECTED);
    }

}
