
package com.github.armedis.http.service;

import java.util.Optional;

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
    public Optional<String> getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "RedisRequest [key=" + key + ", command=" + command + "]";
    }
}
