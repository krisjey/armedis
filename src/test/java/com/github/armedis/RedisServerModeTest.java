
package com.github.armedis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
public class RedisServerModeTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
    * Redis INFO 명령을 실행하고 결과를 Properties 객체로 반환합니다.
    */
    public Properties getRedisInfo() {
        return stringRedisTemplate.execute((RedisCallback<Properties>) connection -> connection.info());
    }

    @Test
    public void test() throws InterruptedException, ExecutionException {
        Properties infoResult = stringRedisTemplate.execute((RedisCallback<Properties>) connection -> connection.serverCommands().info());

        // all servers info
        /*
         * <pre>192.168.56.105:17001.io_threaded_reads_processed : 0
        192.168.56.105:17001.sync_partial_ok : 0
        192.168.56.105:17002.role : master</pre>
         */
        assertThat(infoResult).isNotNull();
        infoResult.forEach((key, value) -> {
            System.out.println(key + " : " + value);
        });
    }

}
