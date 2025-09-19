package com.github.armedis.redis.info;

public record RedisDbInfo(
        String dbName,
        int keys,
        int expires,
        long avgTtl,
        int subExpiry) {

    public static RedisDbInfo fromString(String line) {
        String[] parts = line.split(":");
        String dbName = parts[0].trim();

        String[] kvPairs = parts[1].split(",");
        int keys = Integer.parseInt(kvPairs[0].split("=")[1]);
        int expires = Integer.parseInt(kvPairs[1].split("=")[1]);
        long avgTtl = Long.parseLong(kvPairs[2].split("=")[1]);
        int subExpiry = Integer.parseInt(kvPairs[3].split("=")[1]);

        return new RedisDbInfo(dbName, keys, expires, avgTtl, subExpiry);
    }
}