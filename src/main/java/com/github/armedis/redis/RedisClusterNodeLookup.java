
package com.github.armedis.redis;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.redis.connection.RedisNodeLookup;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.models.partitions.ClusterPartitionParser;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;

public class RedisClusterNodeLookup implements RedisNodeLookup {
    private final Logger logger = LoggerFactory.getLogger(RedisClusterNodeLookup.class);

    @Override
    public Set<RedisNode> lookup(StatefulRedisConnection<String, String> redisSeedConnection) {
        Set<RedisNode> actualServers = new HashSet<>();

        /**
        3b7db20fb131b992a04016ab5278acb19719ab02 192.168.56.104:7001 master - 0 1569309845320 25 connected 0-5460
        893b5edd18e050ab30af439964c00a0cbad09b19 192.168.56.104:7004 slave 3b7db20fb131b992a04016ab5278acb19719ab02 0 1569309843258 25 connected
        d0076af744d0b8f3c96c07b1c063cae714ac09d9 192.168.56.104:7003 myself,slave 59eb1ab8a4af12f9fb82d796701e98403dda4230 0 0 3 connected
        c782d11e3795ebae0c5f91be5b6532843f593a67 192.168.56.104:7002 master - 0 1569309842240 26 connected 5461-10922
        d565af81735ec890fe8b786de879d63472373d0a 192.168.56.104:7005 slave c782d11e3795ebae0c5f91be5b6532843f593a67 0 1569309844289 26 connected
        59eb1ab8a4af12f9fb82d796701e98403dda4230 192.168.56.104:7006 master - 0 1569309840179 21 connected 10923-16383
         */
        try {
            String clusterNodes = redisSeedConnection.async().clusterNodes().get();

            Partitions partitions = ClusterPartitionParser.parse(clusterNodes);

            for (RedisClusterNode node : partitions.getPartitions()) {
                logger.info(node.toString());
                actualServers.add(new RedisNode(node.getUri().getHost(), node.getUri().getPort()));
            }
        }
        catch (InterruptedException | ExecutionException e) {
            logger.error("", e);
        }

        return actualServers;
    }
}
