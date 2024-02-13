package com.github.armedis.redis.info;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisInfoVo {

	@JsonInclude
	private Server server;

	@JsonInclude
	private Clients clients;

	@JsonInclude
	private Memory memory;

	@JsonInclude
	private Persistence persistence;

	@JsonInclude
	private Stats stats;

	@JsonInclude
	private Replication replication;

	@JsonInclude
	private CPU cpu;

	@JsonInclude
	private Modules modules;

	@JsonInclude
	private Errorstats errorstats;

	@JsonInclude
	private Cluster cluster;

	@JsonInclude
	private Map<Integer, Keyspace> keyspace;

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * @return the clients
	 */
	public Clients getClients() {
		return clients;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(Clients clients) {
		this.clients = clients;
	}

	/**
	 * @return the memory
	 */
	public Memory getMemory() {
		return memory;
	}

	/**
	 * @param memory the memory to set
	 */
	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	/**
	 * @return the persistence
	 */
	public Persistence getPersistence() {
		return persistence;
	}

	/**
	 * @param persistence the persistence to set
	 */
	public void setPersistence(Persistence persistence) {
		this.persistence = persistence;
	}

	/**
	 * @return the stats
	 */
	public Stats getStats() {
		return stats;
	}

	/**
	 * @param stats the stats to set
	 */
	public void setStats(Stats stats) {
		this.stats = stats;
	}

	/**
	 * @return the replication
	 */
	public Replication getReplication() {
		return replication;
	}

	/**
	 * @param replication the replication to set
	 */
	public void setReplication(Replication replication) {
		this.replication = replication;
	}

	/**
	 * @return the cpu
	 */
	public CPU getCpu() {
		return cpu;
	}

	/**
	 * @param cpu the cpu to set
	 */
	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	/**
	 * @return the modules
	 */
	public Modules getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(Modules modules) {
		this.modules = modules;
	}

	/**
	 * @return the errorstats
	 */
	public Errorstats getErrorstats() {
		return errorstats;
	}

	/**
	 * @param errorstats the errorstats to set
	 */
	public void setErrorstats(Errorstats errorstats) {
		this.errorstats = errorstats;
	}

	/**
	 * @return the cluster
	 */
	public Cluster getCluster() {
		return cluster;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	/**
	 * @return the keyspace
	 */
	public Map<Integer, Keyspace> getKeyspace() {
		return keyspace;
	}

	/**
	 * @param keyspace the keyspace to set
	 */
	public void setKeyspace(Map<Integer, Keyspace> keyspace) {
		this.keyspace = keyspace;
	}

	public String toJsonString() {
		String json = null;
		try {
			json = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return json;
	}

	// Static method to create an instance of RedisInfoVO from the INFO command
	public static RedisInfoVo fromInfoCommandResult(String infoResult) throws Throwable {
		RedisInfoVo redisInfoVO = new RedisInfoVo();
		String[] sections = infoResult.split("# ");

		for (String section : sections) {
			if (StringUtils.isEmpty(section)) {
				continue;
			}

			String[] lines = section.split("\r\n");

			if (lines.length > 0) {
				String sectionName = lines[0].trim();
				String sectionContent = section.substring(section.indexOf("\r\n") + 2);

				switch (sectionName) {
				case "Server":
					redisInfoVO.setServer(Server.fromString(Server.class, sectionContent));
					break;
				case "Clients":
					redisInfoVO.setClients(Clients.fromString(Clients.class, sectionContent));
					break;
				case "Memory":
					redisInfoVO.setMemory(Memory.fromString(Memory.class, sectionContent));
					break;
				case "Persistence":
					redisInfoVO.setPersistence(Persistence.fromString(Persistence.class, sectionContent));
					break;
				case "Stats":
					redisInfoVO.setStats(Stats.fromString(Stats.class, sectionContent));
					break;
				case "Replication":
					redisInfoVO.setReplication(Replication.fromString(Replication.class, sectionContent));
					break;
				case "CPU":
					redisInfoVO.setCpu(CPU.fromString(CPU.class, sectionContent));
					break;
				case "Modules":
					redisInfoVO.setModules(Modules.fromString(Modules.class, sectionContent));
					break;
				case "Errorstats":
					redisInfoVO.setErrorstats(Errorstats.fromString(Errorstats.class, sectionContent));
					break;
				case "Cluster":
					redisInfoVO.setCluster(Cluster.fromString(Cluster.class, sectionContent));
					break;
				case "Keyspace":
					redisInfoVO.setKeyspace(Keyspace.fromString(sectionContent));
					break;
				}
			}
		}

		return redisInfoVO;
	}
}