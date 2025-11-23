package com.github.armedis.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class LocalIpAddressTest {

    @Test
    public void test() {
        LocalIpAddress localIpAddress = new LocalIpAddress();

        String ipAddress = localIpAddress.getLocalIpAddress();

        // local IP address, This address connectable at out of the system.
        assertThat(ipAddress).isNotNull();
    }
}
