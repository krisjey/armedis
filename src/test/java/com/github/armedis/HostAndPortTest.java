package com.github.armedis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.google.common.net.HostAndPort;

class HostAndPortTest {
    
    @Test
    void test() {
        HostAndPort hp = HostAndPort.fromString("172.28.128.3:7001");
        hp.getHost(); // returns "2001:db8::1"
        hp.getPort(); // returns 80
        hp.toString(); // returns "[2001:db8::1]:80"
        
        assertThat(hp.getHost()).isEqualTo("172.28.128.3");
        assertThat(hp.getPort()).isEqualTo(7001);
        assertThat(hp.toString()).isEqualTo("172.28.128.3:7001");
    }
    
}
