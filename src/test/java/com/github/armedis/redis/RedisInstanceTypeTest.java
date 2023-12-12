
package com.github.armedis.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;

public class RedisInstanceTypeTest {

    @BeforeTestClass
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
        
        assertThatThrownBy(() -> {
            @SuppressWarnings("unused")
            RedisInstanceType typeNull = RedisInstanceType.of(null);
        }).isInstanceOf(NullPointerException.class);
    }

}
