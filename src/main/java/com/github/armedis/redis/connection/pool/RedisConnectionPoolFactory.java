
package com.github.armedis.redis.connection.pool;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.RedisInstanceType;
import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.RedisServerInfoMaker;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.masterreplica.StatefulRedisMasterReplicaConnection;
import io.lettuce.core.support.ConnectionPoolSupport;

@Component
public class RedisConnectionPoolFactory implements RedisConnectionPool<String, String> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private RedisServerInfoMaker redisServerInfoMaker;

    // single connection pool
    private GenericObjectPool<StatefulRedisConnection<String, String>> singleConnectionPool;

    // master connection pool
    private GenericObjectPool<StatefulRedisMasterReplicaConnection<String, String>> masterReplicaConnectionPool;

    // cluster connection pool
    private GenericObjectPool<StatefulRedisClusterConnection<String, String>> clusterConnectionPool;

    @Autowired
    public RedisConnectionPoolFactory(RedisServerInfoMaker redisServerInfoMaker) {
        this.redisServerInfoMaker = redisServerInfoMaker;

        buildConnectonPool();
    }

    private void buildConnectonPool() {
        RedisInstanceType redisServerInfo = this.redisServerInfoMaker.getRedisServerInfo().getRedisInstanceType();
        switch (redisServerInfo) {
            case STANDALONE:
                this.singleConnectionPool = buildStandaloneConnectionPool();
                break;

            case SENTINEL:
                /**
                redisCommand.clientList()
                id=2 addr=192.168.56.104:36756 fd=6 name=sentinel-f1359b20-cmd age=5031 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=32768 obl=0 oll=0 omem=0 events=r cmd=ping
                id=3 addr=192.168.56.104:37058 fd=7 name=sentinel-f1359b20-pubsub age=5031 idle=2 flags=N db=0 sub=1 psub=0 multi=-1 qbuf=0 qbuf-free=0 obl=0 oll=0 omem=0 events=r cmd=subscribe
                 */
                throw new NotImplementedException("Connection pool not implemented " + redisServerInfo.toString());

            case CLUSTER:
                this.clusterConnectionPool = buildClusterConnectionPool();
                break;

            case NOT_DETECTED:
                throw new NotImplementedException("Can not detect connection type " + redisServerInfo.toString());

            default:
                throw new NotImplementedException("Can not detect connection type " + redisServerInfo.toString());
        }
    }

    private GenericObjectPool<StatefulRedisConnection<String, String>> buildStandaloneConnectionPool() {
        requireNonNull(redisServerInfoMaker, "redis server info is null");

        Set<RedisNode> nodes = redisServerInfoMaker.getRedisServerInfo().getRedisNodes();

//        MasterSlaveTopologyProvider masterSlaveTopologyProvider = new MasterSlaveTopologyProvider(connection, redisURI);

        // cluster node
        RedisURI redisNode = null;
        for (RedisNode item : nodes) {
            redisNode = RedisURI.create(item.getHost(), item.getPort());
        }

        RedisClient redisClient = RedisClient.create(redisNode);

        GenericObjectPool<StatefulRedisConnection<String, String>> pool = ConnectionPoolSupport
                .createGenericObjectPool(redisClient::connect, buildBasicConnectionPoolConfig());

        return pool;
    }

    private GenericObjectPool<StatefulRedisClusterConnection<String, String>> buildClusterConnectionPool() {
        requireNonNull(redisServerInfoMaker, "redis server info is null");
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(true) // 주기적 info 명령 호출
                .refreshPeriod(Duration.ofSeconds(30))
                .enableAllAdaptiveRefreshTriggers()
                .build();

        Set<RedisNode> nodes = redisServerInfoMaker.getRedisServerInfo().getRedisNodes();

        // cluster node
        RedisURI clusterNode = null;
        for (RedisNode item : nodes) {
            clusterNode = RedisURI.create(item.getHost(), item.getPort());
        }

        RedisClusterClient clusterClient = RedisClusterClient.create(clusterNode);
        clusterClient.setOptions(ClusterClientOptions.builder()
                .topologyRefreshOptions(topologyRefreshOptions)
                .autoReconnect(true)
                .build());

        GenericObjectPool<StatefulRedisClusterConnection<String, String>> pool = ConnectionPoolSupport
                .createGenericObjectPool(clusterClient::connect, buildBasicConnectionPoolConfig());

        return pool;
    }

    @Override
    public StatefulRedisClusterConnection<String, String> getClusterConnection() throws Exception {
        StatefulRedisClusterConnection<String, String> connection = null;

        connection = clusterConnectionPool.borrowObject();

        return connection;
    }

    private <T> GenericObjectPoolConfig<T> buildBasicConnectionPoolConfig() {
        GenericObjectPoolConfig<T> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxTotal(100);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        // block set 되면 pool이 망가져서 처리안됨.
        poolConfig.setBlockWhenExhausted(false);

        return poolConfig;
    }

    @Override
    public StatefulRedisConnection<String, String> getNonClusterConnection() throws Exception {
        StatefulRedisConnection<String, String> connection = null;

        connection = singleConnectionPool.borrowObject();

        return connection;
    }

    @Override
    public void returnObject(StatefulRedisClusterConnection<String, String> connection) throws Exception {
        clusterConnectionPool.returnObject(connection);
    }

    @Override
    public void returnObject(StatefulRedisConnection<String, String> connection) throws Exception {
        singleConnectionPool.returnObject(connection);
    }
}
