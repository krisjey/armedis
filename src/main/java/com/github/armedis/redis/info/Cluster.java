package com.github.armedis.redis.info;

final class Cluster extends StatsBaseVo {
	private int clusterEnabled;

	public int getClusterEnabled() {
		return clusterEnabled;
	}

	public void setClusterEnabled(int clusterEnabled) {
		this.clusterEnabled = clusterEnabled;
	}
}