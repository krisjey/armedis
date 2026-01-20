/**
 * 
 */
package com.github.armedis.config;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.core.RedisCallback;

/**
 * 
 */
public class RedisCallbackFactory {
    public static final String MAXMEMORY = "maxmemory";
    public static final String TIMEOUT = "timeout";
    public static final String INFO = "INFO";

    private static final Map<String, RedisCallback<String>> CACHE = new ConcurrentHashMap<>();

    public static RedisCallback<String> getConfigCallback(String configKey) {
        return CACHE.computeIfAbsent(configKey, key -> conn -> {
            Properties prop = conn.serverCommands().getConfig(key);
            return (prop == null || prop.isEmpty()) ? null : prop.getProperty(key);
        });
    }

    public static RedisCallback<String> getInfoCallback() {
        return CACHE.computeIfAbsent(INFO, key -> conn -> {
            Object result = conn.execute("INFO");
            return result != null ? (String) result : null;
        });
    }
}
