
package com.github.armedis.redis.connection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.masterreplica.MasterReplica;
import io.lettuce.core.masterreplica.StatefulRedisMasterReplicaConnection;
import io.lettuce.core.masterslave.MasterSlaveTopologyProvider;

public class RedisMasterSlaveTest {
    private static final String TEST_STANDALONE_REDIS_HOST = "192.168.56.104";
    private static final int TEST_STANDALONE_REDIS_MASTER_PORT = 6379;
    private static final int TEST_STANDALONE_REDIS_SLAVE_PORT = 6380;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Ignore
    @Test
    public void testMasterSetAndGet() {
        RedisClient client = RedisClient.create("redis://" + TEST_STANDALONE_REDIS_HOST + ":" + TEST_STANDALONE_REDIS_MASTER_PORT);

        StatefulRedisConnection<String, String> connection = client.connect();

        RedisCommands<String, String> commands = connection.sync();

        String value = null;

        commands.set("foo", "test value");

        value = commands.get("foo");

        assertThat(value).isNotNull();
        assertThat(value).isEqualTo("test value");

        commands.set("foo", "ttext");

        value = commands.get("foo");

        assertThat(value).isNotNull();
        assertThat(value).isEqualTo("ttext");

        connection.close();

        client.shutdown();
    }

    @Test
    public void testSlaveSetAndGet() {
        RedisURI redisURI = RedisURI.create("redis://" + TEST_STANDALONE_REDIS_HOST + ":" + TEST_STANDALONE_REDIS_SLAVE_PORT);
        RedisClient client = RedisClient.create();
        
        StatefulRedisMasterReplicaConnection<String, String> connection = MasterReplica.connect(RedisClient.create(), StringCodec.UTF8, redisURI);
        
//        MasterSlaveTopologyProvider
        
        RedisCommands<String, String> commands = connection.sync();

        String value = null;

        commands.set("foo", "test value");

        value = commands.get("foo");

        assertThat(value).isNotNull();
        assertThat(value).isEqualTo("test value");

        commands.set("foo", "ttext");

        value = commands.get("foo");

        assertThat(value).isNotNull();
        assertThat(value).isEqualTo("ttext");

        connection.close();
    }
}
