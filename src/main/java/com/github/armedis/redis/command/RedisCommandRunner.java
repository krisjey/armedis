
package com.github.armedis.redis.command;

import org.apache.commons.lang3.StringUtils;

public interface RedisCommandRunner {
    static final String REDIS_COMMAND_RUNNER_PREFIX = "redis";
    static final String REDIS_COMMAND_RUNNER_POSTFIX = "CommandRunner";

//    RedisCommandExecuteResult executeAndGet(RedisCommands<String, String> commands);
//
//    RedisCommandExecuteResult executeAndGet(RedisClusterCommands<String, String> commands);

    // TODO executeAndGet(RedisClusterCommands<String, String> commands) 제거 후 아래 메서드로 대체. tps local 10000 tps
    RedisCommandExecuteResult executeAndGet();

    static String getCommandRunnerName(String requestCommand) {
        return REDIS_COMMAND_RUNNER_PREFIX + StringUtils.capitalize(requestCommand) + REDIS_COMMAND_RUNNER_POSTFIX;
    }
}
