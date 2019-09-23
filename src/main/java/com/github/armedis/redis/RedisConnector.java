package com.github.armedis.redis;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.ArmedisServer;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

// @Nullable

public class RedisConnector {
    private final Logger logger = LoggerFactory.getLogger(ArmedisServer.class);

    private boolean isCluster;

    private String seedAddresses;

    private Set<RedisInstance> seedInfo;

    private Set<RedisInstance> actualServers = new HashSet<>();

    /**
     * 
     * @param seedAddresses
     */
    public RedisConnector(String seedAddresses) {
        this.seedAddresses = requireNonNull(seedAddresses);
        this.seedInfo = createRedisSeedInfo(this.seedAddresses);
    }

    private Set<RedisInstance> createRedisSeedInfo(String seedAddresses) {
        Set<RedisInstance> seedRedisInfos = new HashSet<RedisInstance>();
        String[] addresses = seedAddresses.split("[,]");

        for (String address : addresses) {
            if (address.contains(":")) {
                String[] hostAndPort = address.split("[:]");
                String host = hostAndPort[0];
                String port = hostAndPort[1];

                seedRedisInfos.add(new RedisInstance(host, Integer.parseInt(port)));
            }
        }

        return seedRedisInfos;
    }

    /**
     * Lookup redis server by seed<br/>
     * Destination is first connected server.
     * @return 
     */
    public Set<RedisInstance> lookupNodes() {
        Set<RedisInstance> nodes = null;
        // get seed connection
        try (StatefulRedisConnection<String, String> redisSeed = getSeedConnection();) {
            // get nodes
            nodes = detectRedisServerNodes(redisSeed);
        }

//        isCluster = connectionDetector.isCluster();

        return nodes;
    }

    /**
     * Detect redis server nodes
     * @param redisSeed
     * @return
     */
    private Set<RedisInstance> detectRedisServerNodes(StatefulRedisConnection<String, String> redisSeedConnection) {
        Set<RedisInstance> nodes = new HashSet<RedisInstance>();
        // is cluster, master/slave, support sentinel

        System.out.println("Connected to Redis");

        RedisCommands<String, String> syncCommands = redisSeedConnection.sync();
        String redisInfo = syncCommands.info();

        System.out.println("1");
        System.out.println("1");

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
        
        syncCommands.clusterInf o();

        return nodes;
    }

    private StatefulRedisConnection<String, String> getSeedConnection() {
        for (RedisInstance seed : this.seedInfo) {
            try {
                ConnectionDetector connectionDetector = new ConnectionDetector(seed);
                logger.info("Connected server " + seed.toString());

                return connectionDetector.connect();
            }
            catch (Exception e) {
                logger.warn("Can not connect seed server " + seed.toString());
                seed.setValid(false);
            }
        }

        throw new IllegalArgumentException("Can not connect seed server " + this.seedInfo.toString());
    }
}
