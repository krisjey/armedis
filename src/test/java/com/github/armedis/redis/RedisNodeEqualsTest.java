
package com.github.armedis.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class RedisNodeEqualsTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void testEqualsRuleNo1() {
        RedisNode sourceObject1 = new RedisNode("192.168.56.100", 1000);

        // rule no 1
        assertThat(sourceObject1.equals(sourceObject1)).isTrue();
    }

    @Test
    public void testEqualsRuleNo2() {
        RedisNode sourceObject1 = new RedisNode("192.168.56.100", 1000);
        RedisNode sourceObject2 = new RedisNode("192.168.56.100", 1000);

        // rule no 2
        assertThat(sourceObject1.equals(sourceObject2)).isTrue();
        assertThat(sourceObject2.equals(sourceObject1)).isTrue();
    }

    @Test
    public void testEqualsRuleNo3() {
        RedisNode sourceObject1 = new RedisNode("192.168.56.100", 1000);
        RedisNode sourceObject2 = new RedisNode("192.168.56.100", 1000);
        RedisNode sourceObject3 = new RedisNode("192.168.56.100", 1000);

        assertThat(sourceObject1.equals(sourceObject2)).isTrue();
        assertThat(sourceObject2.equals(sourceObject3)).isTrue();
        assertThat(sourceObject1.equals(sourceObject3)).isTrue();
    }

    @Test
    public void testEqualsRuleNo4() {
        RedisNode sourceObject1 = new RedisNode("192.168.56.100", 1000);
        RedisNode sourceObject2 = new RedisNode("192.168.56.100", 1000);
        RedisNode sourceObject3 = new RedisNode("192.168.56.100", 1000);

        // rule no 4
        for (int i = 0; i < 100; i++) {
            assertThat(sourceObject1.equals(sourceObject1)).isTrue();
        }

        for (int i = 0; i < 100; i++) {
            assertThat(sourceObject1.equals(sourceObject2)).isTrue();
        }

        for (int i = 0; i < 100; i++) {
            assertThat(sourceObject2.equals(sourceObject1)).isTrue();
        }

        for (int i = 0; i < 100; i++) {
            assertThat(sourceObject3.equals(sourceObject1)).isTrue();
        }
    }

    @Test
    public void testEqualsRuleNo5() {
        RedisNode sourceObject1 = new RedisNode("192.168.56.100", 1000);
        RedisNode sourceObject2 = new RedisNode("192.168.56.100", 1000);
        RedisNode sourceObject3 = new RedisNode("192.168.56.100", 1000);

        // rule no 5
        assertThat(sourceObject1.equals(null)).isFalse();
        assertThat(sourceObject2.equals(null)).isFalse();
        assertThat(sourceObject3.equals(null)).isFalse();
    }
}
