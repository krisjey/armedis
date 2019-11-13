
package com.github.armedis.redis.command;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface RedisCommandExecuteResult {
    String toResponseString();

    ObjectNode toObjectNode();
}
