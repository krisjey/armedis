
package com.github.armedis.http.service;

import com.linecorp.armeria.server.annotation.Param;

public class RedisRequest extends AbstractRedisParam {
    @Param("body")
    String body;

    @Override
    public String toString() {
        return "RedisRequest [body=" + body + ", key=" + key + ", command=" + command + "]";
    }
}
