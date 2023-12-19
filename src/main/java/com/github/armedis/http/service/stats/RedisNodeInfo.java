package com.github.armedis.http.service.stats;

/**
 * id, ip, listenPort, clusterBusPort, flags, masterId, pingSend, pongRecv,
 * configEpoch, linkState, shardSlotStart, shardSlotEnd
 */
public class RedisNodeInfo {
	private String id;
	private String ip;
	private int listenPort;
	private int clusterBusPort;
	private String flags;
	private String masterId;
	private int pingSend;
	private int pongRecv;
	private long configEpoch;
	private String linkState;
	private int shardSlotStart;
	private int shardSlotEnd;

	/**
	 * @return the id
	 */
	public String id() {
		return id;
	}

	/**
	 * @param id the id to
	 */
	public void id(String id) {
		this.id = id;
	}

	/**
	 * @return the ip
	 */
	public String ip() {
		return ip;
	}

	/**
	 * @param ip the ip to
	 */
	public void ip(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the port
	 */
	public int listenPort() {
		return listenPort;
	}

	/**
	 * @param port the port to
	 */
	public void listenPort(int listenPort) {
		this.listenPort = listenPort;
	}

	/**
	 * @return the clusterBusPort
	 */
	public int clusterBusPort() {
		return clusterBusPort;
	}

	/**
	 * @param clusterBusPort the clusterBusPort to
	 */
	public void clusterBusPort(int clusterBusPort) {
		this.clusterBusPort = clusterBusPort;
	}

	/**
	 * @return the flags
	 */
	public String flags() {
		return flags;
	}

	/**
	 * @param flags the flags to
	 */
	public void flags(String flags) {
		this.flags = flags;
	}

	/**
	 * @return the masterId
	 */
	public String masterId() {
		return masterId;
	}

	/**
	 * @param masterId the masterId to
	 */
	public void masterId(String masterId) {
		this.masterId = masterId;
	}

	/**
	 * @return the pingSend
	 */
	public int pingSend() {
		return pingSend;
	}

	/**
	 * @param pingSend the pingSend to
	 */
	public void pingSend(int pingSend) {
		this.pingSend = pingSend;
	}

	/**
	 * @return the pongRecv
	 */
	public int pongRecv() {
		return pongRecv;
	}

	/**
	 * @param pongRecv the pongRecv to
	 */
	public void pongRecv(int pongRecv) {
		this.pongRecv = pongRecv;
	}

	/**
	 * @return the configEpoch
	 */
	public long configEpoch() {
		return configEpoch;
	}

	/**
	 * @param configEpoch the configEpoch to
	 */
	public void configEpoch(long configEpoch) {
		this.configEpoch = configEpoch;
	}

	/**
	 * @return the linkState
	 */
	public String linkState() {
		return linkState;
	}

	/**
	 * @param linkState the linkState to
	 */
	public void linkState(String linkState) {
		this.linkState = linkState;
	}

	/**
	 * @return the shardSlotStart
	 */
	public int shardSlotStart() {
		return shardSlotStart;
	}

	/**
	 * @param shardSlotStart the shardSlotStart to
	 */
	public void shardSlotStart(int shardSlotStart) {
		this.shardSlotStart = shardSlotStart;
	}

	/**
	 * @return the shardSlotEnd
	 */
	public int shardSlotEnd() {
		return shardSlotEnd;
	}

	/**
	 * @param shardSlotEnd the shardSlotEnd to
	 */
	public void shardSlotEnd(int shardSlotEnd) {
		this.shardSlotEnd = shardSlotEnd;
	}
}