package com.github.armedis.redis;

public class RedisConnector {
	private boolean isCluster;
	private String seedHost;
	private int seedPort;

	/**
	 * 
	 * @param seedHost Connect redis server by seed
	 * @param seedPort Connect redis server by seed
	 */
	public RedisConnector(String seedHost, int seedPort) {
		this.seedHost = seedHost;
		this.seedPort = seedPort;

		ClusterDetector clusterDetector = new ClusterDetector(this.seedHost, this.seedPort);
		isCluster = clusterDetector.isCluster();
	}

	public void connect() {
		
	}
}
