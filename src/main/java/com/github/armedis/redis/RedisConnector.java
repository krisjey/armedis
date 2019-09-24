package com.github.armedis.redis;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisConnector {
    private final Logger logger = LoggerFactory.getLogger(RedisConnector.class);

    private RedisInstanceType redisInstanceType;

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
     * @throws OperationNotSupportedException 
     */
    public Set<RedisInstance> lookupNodes() throws OperationNotSupportedException {
        // get seed connection
        try (StatefulRedisConnection<String, String> redisSeedConnection = getSeedConnection();) {
            // get nodes
            actualServers = detectRedisServerNodes(redisSeedConnection);
        }

        return actualServers;
    }

    /**
     * Detect redis server nodes by seed connection info
     * @param redisSeed
     * @return redis server nodes
     * @throws OperationNotSupportedException 
     */
    private Set<RedisInstance> detectRedisServerNodes(StatefulRedisConnection<String, String> redisSeedConnection) throws OperationNotSupportedException {
        Set<RedisInstance> nodes = null;
        // is cluster, master/slave, support sentinel

        logger.info("Connected to Redis");

        RedisNodeLookup nodeLookup = null;

        RedisCommands<String, String> syncCommands = redisSeedConnection.sync();
        String redisInfo = syncCommands.info();
        
        logger.info(syncCommands.role().toString());

        // TYPE cluster, none cluster, master, slave
        for (String line : redisInfo.split("\\r?\\n")) {

            if (line.startsWith("redis_mode")) {
                String type = line.split("[:]")[1];

                logger.info("Redis node type [" + type + "]");
                redisInstanceType = RedisInstanceType.of(type);
                nodeLookup = RedisLookupFactory.create(redisInstanceType);
                nodes = nodeLookup.lookup(redisSeedConnection);
            }
        }

        if (nodeLookup == null) {
            nodeLookup = RedisLookupFactory.create(RedisInstanceType.NOT_DETECTED);
            nodes = nodeLookup.lookup(redisSeedConnection);
        }

        syncCommands.clusterInfo();

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
