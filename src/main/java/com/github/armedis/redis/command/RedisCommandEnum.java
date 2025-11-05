
package com.github.armedis.redis.command;

import static java.util.Objects.requireNonNull;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Redis command enums<br/>
 * 
 * String, Hash, Set, Sorted Set, List commands
 * name should be lower case.
 * @author krisjey
 *
 */
public enum RedisCommandEnum {
//  NOT_DETECTED("not_detected", NotDetected.class),
    NOT_DETECTED("not_detected"),

    // String commands
    APPEND("append"),
    BITCOUNT("bitcount"),
    BITFIELD("bitfield"),
    BITOP("bitop"),
    BITPOS("bitpos"),
    DECR("decr"),
    DECRBY("decrby"),
    GET("get"),
    GETBIT("getbit"),
    GETRANGE("getrange"),
    GETSET("getset"),
    INCR("incr"),
    INCRBY("incrby"),
    INCRBYFLOAT("incrbyfloat"),
    MGET("mget"),
    MSET("mset"),
    MSETNX("msetnx"),
    PSETEX("psetex"),
    SET("set"),
    SETBIT("setbit"),
    SETEX("setex"),
    SETNX("setnx"),
    SETRANGE("setrange"),
    STRLEN("strlen"),
    TTL("ttl"),
    EXPIRE("expire"),

    // hash commands
    HDEL("hdel"),
    HEXISTS("hexists"),
    HGET("hget"),
    HGETALL("hgetall"),
    HINCRBY("hincrby"),
    HINCRBYFLOAT("hincrbyfloat"),
    HKEYS("hkeys"),
    HLEN("hlen"),
    HMGET("hmget"),
    HMSET("hmset"),
    HSET("hset"),
    HSETNX("hsetnx"),
    HSTRLEN("hstrlen"),
    HVALS("hvals"),
    HSCAN("hscan"),
    HEXPIRE("hexpire"),
    HTTL("httl"),

    // Set commands
    SADD("sadd"),
    SCARD("scard"),
    SDIFF(""),
    SDIFFSTORE("sdiffstore"),
    SINTER("sinter"),
    SINTERSTORE("sinterstore"),
    SISMEMBER("sismember"),
    SMEMBERS("smembers"),
    SMOVE("smove"),
    SPOP("spop"),
    SRANDMEMBER("srandmember"),
    SREM("srem"),
    SUNION("sunion"),
    SUNIONSTORE("sunionstore"),
    SSCAN("sscan"),

    // Sorted Set commands
    BZPOPMIN("bzpopmin"),
    BZPOPMAX("bzpopmax"),
    ZADD("zadd"),
    ZCARD("zcard"),
    ZCOUNT("zcount"),
    ZINCRBY("zincrby"),
    ZINTERSTORE("zinterstore"),
    ZLEXCOUNT("zlexcount"),
    ZPOPMAX("zpopmax"),
    ZPOPMIN("zpopmin"),
    ZRANGE("zrange"),
    ZRANGEBYLEX("zrangebylex"),
    ZREVRANGEBYLEX("zrevrangebylex"),
    ZRANGEBYSCORE("zrangebyscore"),
    ZRANK("zrank"),
    ZREM("zrem"),
    ZREMRANGEBYLEX("zremrangebylex"),
    ZREMRANGEBYRANK("zremrangebyrank"),
    ZREMRANGEBYSCORE("zremrangebyscore"),
    ZREVRANGE("zrevrange"),
    ZREVRANGEBYSCORE("zrevrangebyscore"),
    ZREVRANK("zrevrank"),
    ZSCORE("zscore"),
    ZUNIONSTORE("zunionstore"),
    ZSCAN("zscan"),

    // List commands
    BLPOP("blpop"),
    BRPOP("brpop"),
    BRPOPLPUSH("brpoplpush"),
    LINDEX("lindex"),
    LINSERT("linsert"),
    LLEN("llen"),
    LPOP("lpop"),
    LPUSH("lpush"),
    LPUSHX("lpushx"),
    LRANGE("lrange"),
    LREM("lrem"),
    LSET("lset"),
    LTRIM("ltrim"),
    RPOP("rpop"),
    RPOPLPUSH("rpoplpush"),
    RPUSH("rpush"),
    RPUSHX("rpushx"),

    // Management commands
    CONFIG("config"),
    MEMORY("memory"),
    CLIENT("client"), // Connection management

    // TODO add HyperLogLog command
//    PFADD("pfadd"),
//    PFCOUNT("pfcount"),
//    PFMERGE("pfmerge"),

    // TODO add GEO command
//    GEOADD("geoadd"),
//    GEODIST("geodist"),
//    GEOHASH("geohash"),
//    GEOPOS("geopos"),
//    GEORADIUS("georadius"),
//    GEORADIUSBYMEMBER("georadiusbymember"),

    // TODO PUB/SUB command by GRPC/Thrift
    // TODO Streams command by GRPC/Thrift

    // TODO Should be added support version of every Redis commands. and then check current redis version.
    ;

    private String command;
    private String name;

    RedisCommandEnum(String command) {
        this.command = command;
        this.name = command;
    }

    public String getName() {
        return this.name;
    }

    public String getCommand() {
        return this.command;
    }

    private static final Map<String, RedisCommandEnum> redisCommandNames;
    static {
        final ImmutableMap.Builder<String, RedisCommandEnum> builder = ImmutableMap.builder();
        for (RedisCommandEnum e : values()) {
            builder.put(e.getName(), e);
        }

        redisCommandNames = builder.build();
    }

    /**
     * Returns the class of the specified HTTP status code.
     */
    public static RedisCommandEnum of(String name) {
        name = requireNonNull(name).toLowerCase();

        RedisCommandEnum type = redisCommandNames.get(name);
        if (type == null) {
            type = RedisCommandEnum.NOT_DETECTED;
        }

        return type;
    }
}
