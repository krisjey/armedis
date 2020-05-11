
package com.github.armedis.redis.connection;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisNode;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisServerDetector {
    private final Logger logger = LoggerFactory.getLogger(RedisServerDetector.class);

    private RedisInstanceType redisInstanceType;

    private String seedAddresses;

    private Set<RedisNode> seedInfo;

    private Set<RedisNode> actualServers = new HashSet<>();

    /**
     * 
     * @param seedAddresses
     */
    public RedisServerDetector(String seedAddresses) {
        this.seedAddresses = requireNonNull(seedAddresses);
        this.seedInfo = createRedisSeedInfo(this.seedAddresses);
    }

    private Set<RedisNode> createRedisSeedInfo(String seedAddresses) {
        Set<RedisNode> seedRedisNodes = new HashSet<RedisNode>();
        String[] addresses = seedAddresses.split("[,]");

        for (String address : addresses) {
            if (address.contains(":")) {
                String[] hostAndPort = address.split("[:]");
                String host = hostAndPort[0];
                String port = hostAndPort[1];

                seedRedisNodes.add(new RedisNode(host, Integer.parseInt(port)));
            }
        }

        return seedRedisNodes;
    }

    /**
     * Lookup redis server by seed<br/>
     * Destination is first connected server.
     * @return 
     * @throws OperationNotSupportedException 
     */
    public Set<RedisNode> lookupNodes() throws OperationNotSupportedException {
        // get seed connection
        try (StatefulRedisConnection<String, String> redisSeedConnection = getSeedConnection();) {
            // get nodes
            logger.info("Tring to detect server type.");
            actualServers = detectRedisServerNodes(redisSeedConnection);
            logger.info("Detected servers " + actualServers.toString());
        }

        return actualServers;
    }

    // FIXME standalone, cluster로 먼저 구분하고 standalone이면 (single, master-replica, sentinel 구분 필요.)
    /**
     * Detect redis server nodes by seed connection info
     * @param redisSeed
     * @return redis server nodes
     * @throws OperationNotSupportedException 
     */
    private Set<RedisNode> detectRedisServerNodes(StatefulRedisConnection<String, String> redisSeedConnection)
            throws OperationNotSupportedException {
        Set<RedisNode> nodes = null;
        // is cluster, master/slave, support sentinel, can not found.

        logger.info("Connected to Redis");

        RedisNodeLookup nodeLookup = null;

        RedisCommands<String, String> syncCommands = redisSeedConnection.sync();
        String redisInfo = syncCommands.info();

        logger.info("Role " + syncCommands.role().toString());

        // TYPE cluster, none cluster, master, slave
        for (String line : redisInfo.split("\\r?\\n")) {
            if (line.startsWith("redis_mode")) {
                String type = line.split("[:]")[1];

                logger.info("Redis node type [" + type + "]");
                redisInstanceType = RedisInstanceType.of(type);
                nodeLookup = RedisLookupFactory.create(redisInstanceType, this.seedAddresses);
                nodes = nodeLookup.lookup(redisSeedConnection);
            }
        }

        if (nodeLookup == null) {
            nodeLookup = RedisLookupFactory.create(RedisInstanceType.NOT_DETECTED, this.seedAddresses);
            nodes = nodeLookup.lookup(redisSeedConnection);
        }

        return nodes;
    }

    /**
     * Get the first connected server info from the configured server list.
     * @return
     */
    private StatefulRedisConnection<String, String> getSeedConnection() {
        for (RedisNode seed : this.seedInfo) {
            try {
                RedisSeedConnector redisSeedConnector = new RedisSeedConnector(seed);
                logger.info("Connected server " + seed.toString());

                return redisSeedConnector.connect();
            }
            catch (Exception e) {
                logger.warn("Can not connect seed server " + seed.toString());
                seed.setValid(false);
            }
        }

        throw new IllegalArgumentException("Can not connect seed server " + this.seedInfo.toString());
    }

    public RedisInstanceType getRedisInstanceType() {
        return redisInstanceType;
    }
}
