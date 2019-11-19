
package com.github.armedis.redis;

import com.github.armedis.redis.command.RedisCommandExecuteResult;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

public interface RedisCommandRunner {
    RedisCommandExecuteResult run(RedisCommands<String, String> commands);

    RedisCommandExecuteResult run(RedisClusterCommands<String, String> commands);
}
