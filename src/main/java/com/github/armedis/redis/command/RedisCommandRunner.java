
package com.github.armedis.redis.command;

import org.apache.commons.lang3.StringUtils;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

public interface RedisCommandRunner {
    static final String REDIS_COMMAND_RUNNER_PREFIX = "redis";
    static final String REDIS_COMMAND_RUNNER_POSTFIX = "CommandRunner";

    RedisCommandExecuteResult run(RedisCommands<String, String> commands);

    RedisCommandExecuteResult run(RedisClusterCommands<String, String> commands);

    static String getCommandRunnerName(String requestCommand) {
        return REDIS_COMMAND_RUNNER_PREFIX + StringUtils.capitalize(requestCommand) + REDIS_COMMAND_RUNNER_POSTFIX;
    }
}
