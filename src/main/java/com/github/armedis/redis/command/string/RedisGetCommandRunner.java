
package com.github.armedis.redis.command.string;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.command.RedisCommandExecuteResult;
import com.github.armedis.redis.command.RedisCommandRunner;
import com.github.armedis.redis.command.RedisGetRequest;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

@Component
@Scope("prototype")
public class RedisGetCommandRunner implements RedisCommandRunner {
    private final Logger logger = LoggerFactory.getLogger(RedisGetCommandRunner.class);
    private RedisGetRequest redisRequest;

    public RedisGetCommandRunner(RedisGetRequest redisRequest) {
        this.redisRequest = redisRequest;
    }

    @Override
    public RedisCommandExecuteResult run(RedisCommands<String, String> commands) {
        // TODO Auto-generated method stub
        logger.info(redisRequest.toString());
        return null;
    }

    @Override
    public RedisCommandExecuteResult run(RedisClusterCommands<String, String> commands) {
        logger.info(redisRequest.toString());

        commands.get(this.redisRequest.getKey().get());
        
        // TODO how to make RedisCommandExecuteResult? 
//      make concrete class? 
        return null;
    }
}
