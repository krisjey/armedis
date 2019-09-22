package com.github.armedis.redis;

import java.time.Duration;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

public class ClusterDetector {
	private String seedHost;
	private int seedPort;

	public ClusterDetector(String seedHost, int seedPort) {
		this.seedHost = seedHost;
		this.seedPort = seedPort;
	}

	public boolean isCluster() {
		RedisURI redisURI = new RedisURI(seedHost, seedPort, Duration.ofMillis(5000));
		RedisClient client = RedisClient.create(redisURI);
		StatefulRedisConnection<String, String> connection = client.connect();

		connection.sync().clusterInfo();
		return false;
	}
}
