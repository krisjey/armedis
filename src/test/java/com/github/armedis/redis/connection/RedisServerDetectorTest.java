/**
 * 
 */
package com.github.armedis.redis.connection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.github.armedis.redis.RedisNode;

/**
 * 
 */
class RedisServerDetectorTest {

    @Test
    void singleNodeTest() {
        // single node 6379
        RedisServerDetector redisServerDetector = new RedisServerDetector("192.168.56.105", 6379, "");

        Set<RedisNode> allNodes = redisServerDetector.getAllNodes();
        assertThat(allNodes.size()).isEqualTo(1);

        Set<RedisNode> masterNodes = redisServerDetector.getMasterNodes();
        assertThat(masterNodes.size()).isEqualTo(1);

        Set<RedisNode> replicaNodes = redisServerDetector.getReplicaNodes();
        assertThat(replicaNodes.size()).isEqualTo(0);
    }

    @Test
    void replicaNodeTest() {
        // 1 master 2 slaves 8000, 8101, 8102
        RedisServerDetector redisServerDetector = new RedisServerDetector("192.168.56.105", 8000, "");

        Set<RedisNode> allNodes = redisServerDetector.getAllNodes();
        assertThat(allNodes.size()).isEqualTo(3);

        Set<RedisNode> masterNodes = redisServerDetector.getMasterNodes();
        assertThat(masterNodes.size()).isEqualTo(1);

        Set<RedisNode> replicaNodes = redisServerDetector.getReplicaNodes();
        assertThat(replicaNodes.size()).isEqualTo(2);
    }

    @Test
    void sentinelNodeTest() {
        // 1 master 2 slaves 8500, 8501, 8502
        RedisServerDetector redisServerDetector = new RedisServerDetector("192.168.56.105", 8500, "");

        Set<RedisNode> allNodes = redisServerDetector.getAllNodes();
        assertThat(allNodes.size()).isEqualTo(3);

        Set<RedisNode> masterNodes = redisServerDetector.getMasterNodes();
        assertThat(masterNodes.size()).isEqualTo(1);

        Set<RedisNode> replicaNodes = redisServerDetector.getReplicaNodes();
        assertThat(replicaNodes.size()).isEqualTo(2);
    }

    @Test
    void clusterNodeTest() {
        RedisServerDetector redisServerDetector = new RedisServerDetector("192.168.56.105", 17001, "");

        Set<RedisNode> allNodes = redisServerDetector.getAllNodes();
        assertThat(allNodes.size()).isEqualTo(6);

        Set<RedisNode> masterNodes = redisServerDetector.getMasterNodes();
        assertThat(masterNodes.size()).isEqualTo(3);

        Set<RedisNode> replicaNodes = redisServerDetector.getReplicaNodes();
        assertThat(replicaNodes.size()).isEqualTo(3);
    }

}
