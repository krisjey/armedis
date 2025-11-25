
package com.github.armedis.redis;

import static java.util.Objects.requireNonNull;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public enum RedisInstanceType {
    CLUSTER("cluster"), //
    STANDALONE("standalone"), //
    REPLICA("master_slave"), //
    SENTINEL("sentinel"), //
    NOT_DETECTED("none"), //
    ;
    
    private String name;

    RedisInstanceType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, RedisInstanceType> redisInstanceTypes;
    static {
        final ImmutableMap.Builder<String, RedisInstanceType> builder = ImmutableMap.builder();
        for (RedisInstanceType e : values()) {
            builder.put(e.getName(), e);
        }

        redisInstanceTypes = builder.build();
    }

    /**
     * Returns the class of the specified HTTP status code.
     */
    public static RedisInstanceType of(String name) {
        name = requireNonNull(name);

        RedisInstanceType type = redisInstanceTypes.get(name);
        if (type == null) {
            type = RedisInstanceType.NOT_DETECTED;
        }

        return type;
    }

}
