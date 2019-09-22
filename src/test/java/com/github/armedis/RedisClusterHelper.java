
package com.github.armedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
 

//https://commons.apache.org/proper/commons-pool/examples.html

public class RedisClusterHelper {
    protected static final String REDIS_CLUSTER_ADDRESS = REDIS_CLUSTER_ADDRESS;

    protected static final int REDIS_CLUSTER_TIMEOUT =  REDIS_CLUSTER_TIMEOUT, 10000;

    protected static final int REDIS_CLUSTER_POOL_TIMEOUT = 5;

    protected static final int REDIS_CONNECTION_MAX_COUNT = REDIS_CONNECTION_MAX_COUNT, 20;

    private final GenericObjectPool<StatefulRedisClusterConnection<String, String>> pool;

    private RedisClusterHelper() {
        String redisClusterAddress = REDIS_CLUSTER_ADDRESS;

        String addresses[] = redisClusterAddress.split("[,]");
        Set<RedisURI> clusterNodes = new HashSet<RedisURI>();

        for (String item : addresses) {
            String temp[] = item.split("[:]");

            int port = 0;
            try {
                port = Integer.parseInt(temp[1]);
            }
            catch (NumberFormatException e) {
                port = 99999;
            }

            RedisURI host = RedisURI.Builder.redis(temp[0], port).build();
            clusterNodes.add(host);

            System.out.println(host.toString());
        }

        RedisClusterClient clusterClient = RedisClusterClient.create(clusterNodes);
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(true)
                .refreshPeriod(5, TimeUnit.SECONDS)
                .enableAllAdaptiveRefreshTriggers()
                .build();

        clusterClient.setOptions(ClusterClientOptions.builder()
                .topologyRefreshOptions(topologyRefreshOptions)
                .build());

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(REDIS_CONNECTION_MAX_COUNT);
        poolConfig.setMaxTotal(REDIS_CONNECTION_MAX_COUNT);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setBlockWhenExhausted(true);
//        RedisClient redisClient = RedisClient.create("redis://password@localhost:6379/0");
//        StatefulRedisConnection<String, String> connection = redisClient.connect();
//        RedisCommands<String, String> syncCommands = connection.sync();
//
//        syncCommands.set("key", "Hello, Redis!");
//
//        connection.close();
//        redisClient.shutdown();
        pool = ConnectionPoolSupport.createGenericObjectPool(clusterClient::connect, poolConfig);
    }

    /**
     * 싱글톤 처리를 위한 홀더 클래스, 레디스 연결풀이 포함된 도우미 객체를 반환한다.
     */
    private static class LazyHolder {
        static final RedisClusterHelper INSTANCE = new RedisClusterHelper();
    }

    /**
     * 싱글톤 객체를 가져온다.
     *
     * @return 레디스 도우미객체
     */
    @SuppressWarnings("synthetic-access")
    public static RedisClusterHelper getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * 레디스 클라이언트 연결을 가져온다.<br/>
     * 사용이 끝난 뒤 반드시 @link {@link RedisClusterHelper#returnConnection(RedisClusterClient) returnConnection(RedisClusterClient)}
     * 메서드를 호출하여야 한다.
     *
     * @return 레디스 객체
     * @throws Exception
     */
    public final StatefulRedisClusterConnection<String, String> getConnection() throws Exception {
//        return getConnection(ReadFrom.SLAVE);
        return getConnection(ReadFrom.SLAVE_PREFERRED);
    }

    /**
     * 레디스 클라이언트 연결을 가져온다.<br/>
     * 사용이 끝난 뒤 반드시 @link {@link RedisClusterHelper#returnConnection(RedisClusterClient) returnConnection(RedisClusterClient)}
     * 메서드를 호출하여야 한다.
     *
     * @return 레디스 객체
     * @throws Exception
     */
    public final StatefulRedisClusterConnection<String, String> getConnection(ReadFrom readFrom) throws Exception {
        StatefulRedisClusterConnection<String, String> connection = pool.borrowObject(REDIS_CLUSTER_POOL_TIMEOUT * 1000);

        // TODO connection 가져오다 오류 발생하면 slave가져오기.
        connection.setReadFrom(readFrom);
        return connection;
    }

    /**
     * 레디스 클라이언트 연결을 풀로 돌려준다.
     *
     * @return 레디스 객체
     * @throws IOException
     */
    public final void returnConnection(StatefulRedisClusterConnection<String, String> redisClusterClient) {
        pool.returnObject(redisClusterClient);
    }
}
