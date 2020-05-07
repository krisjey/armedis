
package com.github.armedis.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.BeforeClass;
import org.junit.Test;

public class TempTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() {
        LocalDateTime a = LocalDateTime.now();
        LocalDateTime memberDateTime = LocalDateTime.now().withSecond(0);

        long unixTime = memberDateTime.toEpochSecond(ZoneOffset.ofHours(9));

        System.out.println(a.toString() + "][" + memberDateTime.toString() + " " + unixTime);

        Long one = Long.valueOf(1);
        Long zero = Long.valueOf(0);
//        assertThat(one.equals(zero)).isTrue();
        assertThat(one.equals(Long.valueOf(1))).isTrue();
        assertThat(zero.equals(Long.valueOf(0))).isTrue();

    }

}
