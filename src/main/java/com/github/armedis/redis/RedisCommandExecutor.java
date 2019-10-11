
package com.github.armedis.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.http.service.RedisRequest;

@Component
public class RedisCommandExecutor {
    @Autowired

    public ObjectNode execute(RedisRequest redisRequest) {
        redisRequest.getCommand();
        // FIXME should be impl.
        return null;
    }
}
