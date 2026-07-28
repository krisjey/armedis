package com.github.armedis.redis.connection;

/**
 * Redis 노드 역할
 */
public enum RedisNodeRole {
    MASTER,
    REPLICA,
    SENTINEL
}