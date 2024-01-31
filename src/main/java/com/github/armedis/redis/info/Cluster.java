package com.github.armedis.redis.info;

import com.google.common.base.CaseFormat;

final class Cluster {
	private int clusterEnabled;

	/**
	 * TODO from string에서 getter/Setter 찾는 로직 넣기.
	 * 
	 * @param content
	 * @return
	 */
	public static Cluster convert(String content) {
		Cluster cluster = new Cluster();
		String[] lines = content.split("\r\n");

		for (String line : lines) {
			String[] parts = line.split(":");
			if (parts.length == 2) {
				String key = parts[0].trim();
				String value = parts[1].trim();

				key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);

				RedisInfoConverter.setField(cluster, key, value);
			}
		}

		return cluster;
	}

	public int getClusterEnabled() {
		return clusterEnabled;
	}

	public void setClusterEnabled(int clusterEnabled) {
		this.clusterEnabled = clusterEnabled;
	}
}