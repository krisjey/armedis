/**
 * 
 */
package com.github.armedis.redis.connection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.armedis.redis.RedisNode;

/**
 * 
 */
@Disabled
class RedisServerDetectorTest {
    @Test
    void singleNodeTest() {
        // single node 6379
        checkAndCreateDetector("singleTest", "192.168.56.105", 6379, "", 1, 1, 0);
    }

    @Test
    void replicaNodeTest1() {
        // 1 master 2 slaves 8000, 8101, 8102
        checkAndCreateDetector("replicaTest", "192.168.56.105", 8000, "", 3, 1, 2);
        checkAndCreateDetector("replicaTest", "192.168.56.105", 8101, "", 3, 1, 2);
        checkAndCreateDetector("replicaTest", "192.168.56.105", 8102, "", 3, 1, 2);
    }

    @Test
    void sentinelDataNodeTest() {
        RedisServerDetector redisServerDetector = null;
        // 1 master 2 slaves 8500, 8501, 8502
        redisServerDetector = checkAndCreateDetector("sentinelDataTest", "192.168.56.105", 8500, "", 3, 1, 2);
        System.out.println("sentinelDataNodeTest " + redisServerDetector.getAllNodes().toString());

        redisServerDetector = checkAndCreateDetector("sentinelDataTest", "192.168.56.105", 8501, "", 3, 1, 2);
        System.out.println("sentinelDataNodeTest " + redisServerDetector.getAllNodes().toString());

        redisServerDetector = checkAndCreateDetector("sentinelDataTest", "192.168.56.105", 8502, "", 3, 1, 2);
        System.out.println("sentinelNodeTest All " + redisServerDetector.getAllNodes().toString());
    }

    @Test
    void sentinelNodeTest() {
        RedisServerDetector redisServerDetector = null;
        // 1 master 2 slaves 8500, 8501, 8502
        redisServerDetector = checkAndCreateDetector("sentinelTest", "192.168.56.105", 26379, "", 3, 1, 2);
        System.out.println("sentinelNodeTest All " + redisServerDetector.getAllNodes().toString());
        System.out.println("sentinelNodeTest Sen " + redisServerDetector.getSentinelNodes().toString());

        redisServerDetector = checkAndCreateDetector("sentinelTest", "192.168.56.105", 26380, "", 3, 1, 2);
        System.out.println("sentinelNodeTest All " + redisServerDetector.getAllNodes().toString());
        System.out.println("sentinelNodeTest Sen " + redisServerDetector.getSentinelNodes().toString());

        redisServerDetector = checkAndCreateDetector("sentinelTest", "192.168.56.105", 26381, "", 3, 1, 2);
        System.out.println("sentinelNodeTest All " + redisServerDetector.getAllNodes().toString());
        System.out.println("sentinelNodeTest Sen " + redisServerDetector.getSentinelNodes().toString());
    }

    @Test
    void clusterNodeTest() {
        checkAndCreateDetector("clusterTest", "192.168.56.105", 17001, "", 6, 3, 3);
        checkAndCreateDetector("clusterTest", "192.168.56.105", 17002, "", 6, 3, 3);
        checkAndCreateDetector("clusterTest", "192.168.56.105", 17003, "", 6, 3, 3);
        checkAndCreateDetector("clusterTest", "192.168.56.105", 17004, "", 6, 3, 3);
        checkAndCreateDetector("clusterTest", "192.168.56.105", 17005, "", 6, 3, 3);
        checkAndCreateDetector("clusterTest", "192.168.56.105", 17006, "", 6, 3, 3);
    }

    private RedisServerDetector checkAndCreateDetector(String testName, String seedHost, Integer seedPort, String password, int allNodeCnt, int masterNodeCnt, int replicaNodeCnt) {
        RedisServerDetector redisServerDetector = new RedisServerDetector(seedHost, seedPort, password);

        Set<RedisNode> allNodes = redisServerDetector.getAllNodes();
        assertThat(allNodes.size()).isEqualTo(allNodeCnt);

        Set<RedisNode> masterNodes = redisServerDetector.getMasterNodes();
        assertThat(masterNodes.size()).isEqualTo(masterNodeCnt);
        masterNodes.stream().forEach(node -> {
            assertThat(node.getRole()).isEqualTo(RedisNodeRole.MASTER);
        });

        Set<RedisNode> replicaNodes = redisServerDetector.getReplicaNodes();
        assertThat(replicaNodes.size()).isEqualTo(replicaNodeCnt);
        replicaNodes.stream().forEach(node -> {
            assertThat(node.getRole()).isEqualTo(RedisNodeRole.REPLICA);
        });

        System.out.println(testName + " seedHost [" + seedHost + ":" + seedPort + "] " + allNodes.toString());

        return redisServerDetector;
    }

}
