
package com.github.armedis.redis.connection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.redis.RedisNode;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisNoneClusterNodeLookup implements RedisNodeLookup {
    private final Logger logger = LoggerFactory.getLogger(RedisNoneClusterNodeLookup.class);
    private String seedAddresses;

    public RedisNoneClusterNodeLookup(String seedAddresses) {
        this.seedAddresses = seedAddresses;
    }

    /**
     *  master ==>
     *         role:master
     *         connected_slaves:2
     *         slave0:ip=192.168.56.104,port=6380,state=online,offset=2482,lag=1
     *         slave1:ip=192.168.56.104,port=6381,state=online,offset=2482,lag=1
     * 
     *  slave ==>
     *         role:slave
     *         master_host:192.168.56.104
     *         master_port:6379
     */
    @Override
    public Set<RedisNode> lookup(StatefulRedisConnection<String, String> redisSeedConnection) {
        Set<RedisNode> actualServers = new HashSet<>();

        try {
            RedisNode masterNode = findMasterNode(redisSeedConnection.sync(), seedAddresses);
            actualServers.add(masterNode);

            List<RedisNode> slaveNodes = findSlaves(masterNode);
            if (slaveNodes != null && slaveNodes.size() > 0) {
                actualServers.addAll(slaveNodes);
            }
        }
        catch (Exception e) {
            logger.error("", e);
        }

        return actualServers;
    }

    private List<RedisNode> findSlaves(RedisNode masterNode) {
        RedisClient client = RedisClient.create(RedisURI.create(masterNode.getHost(), masterNode.getPort()));
        try (StatefulRedisConnection<String, String> connection = client.connect();) {
            String replicationInfo = connection.sync().info("Replication");
            return retreveSlaveNodes(replicationInfo);
        }
    }

    private List<RedisNode> retreveSlaveNodes(String replicationInfo) {
        List<RedisNode> slaveNodes = new ArrayList<RedisNode>();

        for (String line : replicationInfo.split("\\r?\\n")) {
            if (line.startsWith("slave")) {
                String slaveHost = null;
                String slavePort = null;
                for (String item : line.split("[:]")[1].split("[,]")) {
                    if (item.startsWith("ip")) {
                        slaveHost = item.split("[=]")[1];
                    }
                    if (item.startsWith("port")) {
                        slavePort = item.split("[=]")[1];
                    }
                }
                slaveNodes.add(new RedisNode(slaveHost, Integer.parseInt(slavePort)));
            }
        }

        return slaveNodes;
    }

    // TODO support multi level replication
    private RedisNode findMasterNode(RedisCommands<String, String> redisCommand, String seedAddresses) {
        String info = redisCommand.info("Replication");

        boolean isSlave = false;
        int connectedSlaves = 0;
        String masterHost = null;
        int masterPort = 0;

        // FIXME readAll after process.
        for (String line : info.split("\\r?\\n")) {
            if (line.startsWith("master_host")) {
                masterHost = line.split("[:]")[1];
            }

            if (line.startsWith("master_port")) {
                masterPort = Integer.parseInt(line.split("[:]")[1]);
            }

            if (line.startsWith("connected_slaves")) {
                connectedSlaves = Integer.parseInt(line.split("[:]")[1]);
            }

            if (line.startsWith("role")) {
                String type = line.split("[:]")[1];

                if ("master".equals(type.toLowerCase())) {
                    if (connectedSlaves == 0) {
                        masterHost = seedAddresses.split(":")[0];
                    }
                    else {
                        masterHost = detectMasterHost(info);
                    }

                    masterPort = Integer.parseInt(redisCommand.configGet("port").get("port"));
                }
                else if ("slave".equals(type.toLowerCase())) {
                    isSlave = true;
                }
            }
        }

        if (isSlave) {
            RedisClient client = RedisClient.create(RedisURI.create(masterHost, masterPort));
            try (StatefulRedisConnection<String, String> connection = client.connect();) {
                return findMasterNode(connection.sync(), seedAddresses);
            }
        }

        return new RedisNode(masterHost, masterPort);
    }

    private String detectMasterHost(String info) {
        String slaveHost = null;
        String slavePort = null;

        for (String line : info.split("\\r?\\n")) {
            // slave0:ip=192.168.56.104,port=6380,state=online,offset=762140,lag=1
            if (line.startsWith("slave0:")) {
                for (String item : line.split("[:]")[1].split("[,]")) {
                    if (item.startsWith("ip")) {
                        slaveHost = item.split("[=]")[1];
                    }
                    if (item.startsWith("port")) {
                        slavePort = item.split("[=]")[1];
                    }
                }
                break;
            }
        }

        String masterAddress = null;
        RedisClient client = RedisClient.create(RedisURI.create(slaveHost, Integer.parseInt(slavePort)));
        try (StatefulRedisConnection<String, String> connection = client.connect();) {
            masterAddress = connection.sync().configGet("slaveof").get("slaveof");
        }

        return masterAddress.split(" ")[0];
    }
}
