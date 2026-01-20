
package com.github.armedis.redis.command;

import org.apache.commons.lang3.StringUtils;

public interface RedisCommandRunner {
    static final String REDIS_COMMAND_RUNNER_PREFIX = "redis";
    static final String REDIS_COMMAND_RUNNER_POSTFIX = "CommandRunner";

    RedisCommandExecuteResult executeAndGet();

    static String getCommandRunnerName(String requestCommand) {
        return REDIS_COMMAND_RUNNER_PREFIX + StringUtils.capitalize(requestCommand) + REDIS_COMMAND_RUNNER_POSTFIX;
    }
}
