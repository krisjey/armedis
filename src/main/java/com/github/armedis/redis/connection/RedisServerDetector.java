
package com.github.armedis.redis.connection;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.RedisNodeType;
import com.github.armedis.redis.info.RedisInfoVo;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisServerDetector {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RedisInstanceType redisInstanceType;

    private String seedAddresses;

    private Set<RedisNode> seedInfo;

    private static Set<RedisNode> allServers = new HashSet<>();
    private static Set<RedisNode> masterServers = new HashSet<>();
    private static Set<RedisNode> replicaServers = new HashSet<>();

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
     * @throws UnsupportedOperationException 
     */
    public Set<RedisNode> lookupNodes() throws UnsupportedOperationException {
        // get seed connection
        try (StatefulRedisConnection<String, String> redisSeedConnection = getSeedConnection();) {
            // get nodes
            logger.info("Tring to detect server type.");
            allServers = detectRedisServerNodes(redisSeedConnection);

            for (RedisNode node : allServers) {
                if (node.getRedisNodeType().equals(RedisNodeType.REPLICA)) {
                    replicaServers.add(node);
                }
                else {
                    masterServers.add(node);
                }
            }

            logger.info("Detected servers " + allServers.toString());
        }

        return allServers;
    }

    // FIXME standalone, cluster로 먼저 구분하고 standalone이면 (single, master-replica, sentinel 구분 필요.)
    /**
     * Detect redis server nodes by seed connection info
     * @param redisSeed
     * @return redis server nodes
     * @throws UnsupportedOperationException 
     */
    private Set<RedisNode> detectRedisServerNodes(StatefulRedisConnection<String, String> redisSeedConnection)
            throws UnsupportedOperationException {
        Set<RedisNode> nodes = null;

        // is cluster, master/slave, support sentinel, can not found.
        logger.info("Connected to Redis");

        RedisNodeLookup nodeLookup = null;

        RedisCommands<String, String> syncCommands = redisSeedConnection.sync();
        String redisInfo = syncCommands.info();

        RedisInfoVo redisInfoVo = null;

        try {
            redisInfoVo = RedisInfoVo.from(redisInfo, false);
        }
        catch (Throwable e) {
            logger.error("Can not parse redis info command result!", e);
        }

        logger.info("Role " + syncCommands.role().toString());

        // TYPE cluster, none cluster, master, slave
        String type = redisInfoVo.getServer().getRedisMode();

        logger.info("Redis node type [" + type + "]");
        redisInstanceType = RedisInstanceType.of(type);

        nodeLookup = RedisLookupFactory.create(redisInstanceType, this.seedAddresses);
        nodes = nodeLookup.lookup(redisSeedConnection);

//        if (nodeLookup == null) {
//            nodeLookup = RedisLookupFactory.create(RedisInstanceType.NOT_DETECTED, this.seedAddresses);
//            nodes = nodeLookup.lookup(redisSeedConnection);
//        }

        return nodes;
    }

    /**
     * Get the first connected server info from the configured server list.
     * @return
     */
    private StatefulRedisConnection<String, String> getSeedConnection() {
        for (RedisNode seed : this.seedInfo) {
            try {
                RedisConnector redisSeedConnector = new RedisConnector(seed);
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

    public static final Set<RedisNode> getAllNodes() {
        return allServers;
    }

    public static final Set<RedisNode> getMasterNodes() {
        return masterServers;
    }

    public static final Set<RedisNode> getReplicaNodes() {
        return replicaServers;
    }
}
