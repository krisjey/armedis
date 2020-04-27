
package com.github.armedis.redis.connection;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.redis.RedisNode;

import io.lettuce.core.api.StatefulRedisConnection;

public class RedisNoneClusterNodeLookup implements RedisNodeLookup {
    private final Logger logger = LoggerFactory.getLogger(RedisNoneClusterNodeLookup.class);

    @Override
    public Set<RedisNode> lookup(StatefulRedisConnection<String, String> redisSeedConnection) {
        Set<RedisNode> actualServers = new HashSet<>();

        /**
        // master ==>
        //        role:master
        //        connected_slaves:2
        //        slave0:ip=192.168.56.104,port=6380,state=online,offset=2482,lag=1
        //        slave1:ip=192.168.56.104,port=6381,state=online,offset=2482,lag=1
        
        // slave ==>
        //        role:slave
        //        master_host:192.168.56.104
        //        master_port:6379
         */
        try {
            String info = redisSeedConnection.sync().info("Replication");
            logger.info(info);

            // seed node is master ==> get slaves
            // seed node is one of slaves ==> get master ==> get slaves

            // add master and slaves.
            // FIXME make code.
//            actualServers.add(new RedisNode(redisSeedConnection, node.getUri().getPort()));
        }
        catch (Exception e) {
            logger.error("", e);
        }

        return actualServers;
    }

}
