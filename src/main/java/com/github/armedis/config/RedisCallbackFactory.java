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
    private static final String COMMANDSTATS = "COMMANDSTATS";
    public static final String CLUSTER_NODES = "CLUSTER_NODES";
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
    
    public static RedisCallback<String> getCommandStatsCallback() {
        return CACHE.computeIfAbsent(COMMANDSTATS, key -> conn -> {
            Object result = conn.execute("INFO", COMMANDSTATS.getBytes());
            return result != null ? (String) result : null;
        });
    }

    public static RedisCallback<String> getClusterNodesCallback() {
        return CACHE.computeIfAbsent(CLUSTER_NODES, key -> conn -> {
            byte[] result = (byte[]) conn.execute("CLUSTER", "NODES".getBytes());
            return result != null ? new String(result) : null;
        });
    }

    public static RedisCallback<Void> setConfigCallback(String configKey, String value) {
        return conn -> {
            conn.serverCommands().setConfig(configKey, value);
            return null;
        };
    }
}
