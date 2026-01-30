
package com.github.armedis.redis;

import java.util.Objects;

import com.github.armedis.redis.connection.RedisNodeRole;

public class RedisNode {
    private final String host;
    private final int port;
    private final RedisNodeRole role;
    private final String key;

    public RedisNode(String host, int port, RedisNodeRole role) {
        this.host = host;
        this.port = port;
        this.role = role;
        this.key = host + ":" + port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public RedisNodeRole getRole() {
        return role;
    }

    public String toKey() {
        return this.key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RedisNode redisNode = (RedisNode) o;
        return port == redisNode.port && Objects.equals(host, redisNode.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }

    @Override
    public String toString() {
        return "RedisNode [host=" + host + ", port=" + port + ", role=" + role + "]";
    }
}
