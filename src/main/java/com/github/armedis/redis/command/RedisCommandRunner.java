
package com.github.armedis.redis.command;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

public interface RedisCommandRunner {
    static final String REDIS_COMMAND_RUNNER_POSTFIX = "RedisCommandRunner";

    RedisCommandExecuteResult run(RedisCommands<String, String> commands);

    RedisCommandExecuteResult run(RedisClusterCommands<String, String> commands);

    static String getCommandRunnerName(String requestCommand) {
        return requestCommand + REDIS_COMMAND_RUNNER_POSTFIX;
    }
}
