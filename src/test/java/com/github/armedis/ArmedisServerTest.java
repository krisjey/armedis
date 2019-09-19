package com.github.armedis;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;

import org.junit.BeforeClass;
import org.junit.Test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;

public class ArmedisServerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() throws InterruptedException, ExecutionException {
        // detect redis type.
        
        // Create connection pool factory 
        
        // 
        
        RedisClient redisClient = RedisClient.create("redis://192.168.56.104:6479/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();

        System.out.println("Connected to Redis");

        RedisFuture<String> info = connection.async().info( );
        
        System.out.println("1");
        System.out.println("1");
        System.out.println("== hello == ");
        System.out.println(info.get());
        System.out.println("1");
        System.out.println("1");

//        connection.sync().set("kriskey", "Hello World");

        connection.close();
        redisClient.shutdown();
    }

}
