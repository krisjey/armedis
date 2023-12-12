
package com.github.armedis.redis.connection;

import org.junit.jupiter.api.Test;

//@SpringBootTest
public class RedisConnectionPoolImplTest {

    @Test
    public void test() {

        // master ==>
//      role:master
//      connected_slaves:2
//      slave0:ip=192.168.56.104,port=6380,state=online,offset=2482,lag=1
//      slave1:ip=192.168.56.104,port=6381,state=online,offset=2482,lag=1

        // slave ==>
//      role:slave
//      master_host:192.168.56.104
//      master_port:6379
//        assertThat(impl.getConnection()).;
    }

}
