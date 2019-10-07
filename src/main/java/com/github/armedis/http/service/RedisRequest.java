
package com.github.armedis.http.service;

import com.linecorp.armeria.server.annotation.Default;
import com.linecorp.armeria.server.annotation.Param;

public class RedisRequest extends AbstractRedisParam {
    @Param("body")
    String body;

    @Param("command")
    @Default("a")
    String command;

    @Override
    public String toString() {
        return "RedisRequest [body=" + body + ", key=" + key + ", command=" + command + "]";
    }

    public String getCommand() {
        return command;
    }
}
