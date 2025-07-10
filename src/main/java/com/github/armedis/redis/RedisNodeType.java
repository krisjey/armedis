
package com.github.armedis.redis;

import static java.util.Objects.requireNonNull;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public enum RedisNodeType {
    MASTER("master"), //
    REPLICA("replica"), //
    SLAVE("slave"), //
    SENTINEL("sentinel"), //
    NOT_DETECTED("none"), //
    ;

    private String name;

    RedisNodeType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, RedisNodeType> redisInstanceTypes;
    static {
        final ImmutableMap.Builder<String, RedisNodeType> builder = ImmutableMap.builder();
        for (RedisNodeType e : values()) {
            builder.put(e.getName(), e);
        }

        redisInstanceTypes = builder.build();
    }

    /**
     * Returns the class of the specified HTTP status code.
     */
    public static RedisNodeType of(String name) {
        name = requireNonNull(name);

        RedisNodeType type = redisInstanceTypes.get(name);
        if (type == null) {
            type = RedisNodeType.NOT_DETECTED;
        }

        return type;
    }

}
