
package com.github.armedis.redis;

import static java.util.Objects.requireNonNull;

import io.lettuce.core.RedisURI;

public class RedisNode {
    private String host;
    private int port;

    private boolean valid;

    private RedisInstanceType instanceType;

    private RedisNodeType redisNodeType;

    private RedisURI uri;

    public RedisNode(String host, int port) {
        this(requireNonNull(host), port, RedisInstanceType.NOT_DETECTED, RedisNodeType.NOT_DETECTED);
    }

    public RedisNode(String host, int port, RedisInstanceType instanceType) {
        this(requireNonNull(host), port, instanceType, RedisNodeType.NOT_DETECTED);
    }

    public RedisNode(String host, int port, RedisNodeType redisNodeType) {
        this(requireNonNull(host), port, RedisInstanceType.NOT_DETECTED, redisNodeType);
    }

    public RedisNode(String host, int port, RedisInstanceType instanceType, RedisNodeType redisNodeType) {
        this.host = requireNonNull(host);
        this.port = port;
        this.instanceType = instanceType;
        this.redisNodeType = redisNodeType;

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

    public RedisNodeType getRedisNodeType() {
        return redisNodeType;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + port;
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass())
            return false;

        RedisNode other = (RedisNode) obj;
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        }
        else if (!host.equals(other.host)) {
            return false;
        }

        if (port != other.port) {
            return false;
        }

        if (uri == null) {
            if (other.uri != null) {
                return false;
            }
        }
        else if (!uri.equals(other.uri)) {
            return false;
        }

        return true;
    }
}
