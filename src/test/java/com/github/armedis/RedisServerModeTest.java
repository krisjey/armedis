
package com.github.armedis;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import com.github.armedis.config.ArmedisConfiguration;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

@ActiveProfiles("testbed")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class RedisServerModeTest {
    @Autowired
    private ArmedisConfiguration armedisConfiguration;

    @BeforeTestClass
    public static void setUpBeforeClass() throws Exception {
    }

//    @Disabled
    @Test
    public void test() throws InterruptedException, ExecutionException {
        String seedHost = armedisConfiguration.getRedisSeedHost();
        int seedPort = armedisConfiguration.getRedisSeedPort();

        RedisURI uri = RedisURI.create(seedHost, seedPort);
        RedisClient redisClient = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connection = redisClient.connect();

        System.out.println("Connected to Redis");

        RedisAsyncCommands<String, String> asyncCommands = connection.async();
        RedisFuture<String> info = asyncCommands.info();

        System.out.println("1");
        System.out.println("1");
        System.out.println("== hello == ");
        System.out.println(info.get());
        System.out.println("1");
        System.out.println("1");

        String redisInfo = info.get();

        // TYPE cluster, none cluster, master, slave
        for (String item : redisInfo.split("\n")) {
            System.out.println(item);
            if (item.startsWith("cluster_enabled")) {
                if (item.equals("cluster_enabled:0")) {
                    // stand alone
                    System.out.println("Stand alone mode");
                }
                else {
                    // cluster
                    System.out.println("Cluster nodes");
                }
            }
        }

//        connection.sync().set("kriskey", "Hello World");

        connection.close();
        redisClient.shutdown();
    }

}
