
package com.github.armedis.http.service;

import com.linecorp.armeria.server.annotation.Default;
import com.linecorp.armeria.server.annotation.Param;

/**
 * Json to redis command object
 *     // http/command with key 
 *     // http/command
 *     // json/command with key
 *     // json/command
 *     
 *     4가지 조합
 * @author kris
 *
 */
public class RedisRequest extends AbstractRedisParam {
    @Param("body")
    String body;

    @Override
    public String toString() {
        return "RedisRequest [body=" + body + ", key=" + key + ", command=" + command + "]";
    }

    public String getCommand() {
        return command;
    }
}
