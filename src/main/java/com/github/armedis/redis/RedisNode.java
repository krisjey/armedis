package com.github.armedis.redis;

import static java.util.Objects.requireNonNull;

import io.lettuce.core.RedisURI;

public class RedisNode {
    private String host;
    private int port;

    private boolean valid;

    private RedisInstanceType instanceType;

    private RedisURI uri;

    public RedisNode(String host, int port) {
        this(requireNonNull(host), port, RedisInstanceType.NOT_DETECTED);
    }

    private RedisNode(String host, int port, RedisInstanceType instanceType) {
        this.host = host;
        this.port = port;
        this.instanceType = instanceType;

        this.uri = RedisURI.create(host, port);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public RedisInstanceType getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(RedisInstanceType instanceType) {
        this.instanceType = requireNonNull(instanceType);
    }

    public RedisURI getUri() {
        return this.uri;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "RedisInstance [host=" + host + ", port=" + port + "]";
    }
}
