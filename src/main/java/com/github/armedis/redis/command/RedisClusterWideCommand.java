
package com.github.armedis.redis.command;

import static java.util.Objects.requireNonNull;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Redis command enums<br/>
 * 
 * String, Hash, Set, Sorted Set, List commands
 * name should be lower case.
 * @author krisjey
 *
 */
public enum RedisClusterWideCommand {
//  NOT_DETECTED("not_detected", NotDetected.class),
    NOT_DETECTED("not_detected"),

    // String commands
    ALL("all"),
    MASTER("master"),
    SLAVE("slave"),
    ;

    private String mode;

    RedisClusterWideCommand(String mode) {
        this.mode = mode;
    }

    public String getName() {
        return this.mode;
    }

    public String getMode() {
        return this.mode;
    }

    private static final Map<String, RedisClusterWideCommand> redisClusterWideNames;
    static {
        final ImmutableMap.Builder<String, RedisClusterWideCommand> builder = ImmutableMap.builder();
        for (RedisClusterWideCommand e : values()) {
            builder.put(e.getName(), e);
        }

        redisClusterWideNames = builder.build();
    }

    /**
     * Returns the class of the specified HTTP status code.
     */
    public static RedisClusterWideCommand of(String name) {
        name = requireNonNull(name).toLowerCase();

        RedisClusterWideCommand type = redisClusterWideNames.get(name);
        if (type == null) {
            type = RedisClusterWideCommand.NOT_DETECTED;
        }

        return type;
    }
}
