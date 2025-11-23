package com.github.armedis.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Redis 연결 설정을 위한 Properties 클래스
 * application.yml의 config.redis.seed 정보를 바인딩
 */
@Component
@ConfigurationProperties(prefix = "config.redis.seed")
public class RedisProperties {
    
    private String host;
    private int port;
    private String password;

    public RedisProperties() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean hasPassword() {
        return password != null && !password.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "RedisProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", password='" + (hasPassword() ? "***" : "none") + '\'' +
                '}';
    }
}