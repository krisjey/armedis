package com.github.armedis.redis;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis info command result object Converted by info command result
 * 
 * <pre>
 * # Server
 * redis_version:6.2.14
 * redis_git_sha1:00000000
 * redis_git_dirty:0
 * redis_build_id:a712fce3205cb7ee
 * redis_mode:cluster
 * os:Linux 3.10.0-1160.105.1.el7.x86_64 x86_64
 * arch_bits:64
 * monotonic_clock:POSIX clock_gettime
 * multiplexing_api:epoll
 * atomicvar_api:atomic-builtin
 * gcc_version:4.8.5
 * process_id:11957
 * process_supervised:no
 * run_id:399bd2da1cf56e78d0a34a54d863d6fcf9e676ae
 * tcp_port:17001
 * server_time_usec:1706081693568294
 * uptime_in_seconds:23884
 * uptime_in_days:0
 * hz:10
 * configured_hz:10
 * lru_clock:11582877
 * executable:/data/redis/cluster/node1/./redis-server
 * config_file:/data/redis/cluster/node1/./redis.conf
 * io_threads_active:0
 * 
 * # Clients
 * connected_clients:1
 * cluster_connections:10
 * maxclients:4064
 * client_recent_max_input_buffer:32
 * client_recent_max_output_buffer:0
 * blocked_clients:0
 * tracking_clients:0
 * clients_in_timeout_table:0
 * 
 * # Memory
 * used_memory:2411832
 * used_memory_human:2.30M
 * used_memory_rss:10231808
 * used_memory_rss_human:9.76M
 * used_memory_peak:2469984
 * used_memory_peak_human:2.36M
 * used_memory_peak_perc:97.65%
 * used_memory_overhead:2294880
 * used_memory_startup:1205192
 * used_memory_dataset:116952
 * used_memory_dataset_perc:9.69%
 * allocator_allocated:2473072
 * allocator_active:2764800
 * allocator_resident:5283840
 * total_system_memory:4388077568
 * total_system_memory_human:4.09G
 * used_memory_lua:30720
 * used_memory_lua_human:30.00K
 * used_memory_scripts:0
 * used_memory_scripts_human:0B
 * number_of_cached_scripts:0
 * maxmemory:0
 * maxmemory_human:0B
 * maxmemory_policy:noeviction
 * allocator_frag_ratio:1.12
 * allocator_frag_bytes:291728
 * allocator_rss_ratio:1.91
 * allocator_rss_bytes:2519040
 * rss_overhead_ratio:1.94
 * rss_overhead_bytes:4947968
 * mem_fragmentation_ratio:4.32
 * mem_fragmentation_bytes:7862744
 * mem_not_counted_for_evict:0
 * mem_replication_backlog:1048576
 * mem_clients_slaves:20512
 * mem_clients_normal:20496
 * mem_aof_buffer:0
 * mem_allocator:jemalloc-5.1.0
 * active_defrag_running:0
 * lazyfree_pending_objects:0
 * lazyfreed_objects:0
 * 
 * # Persistence
 * loading:0
 * current_cow_size:0
 * current_cow_size_age:0
 * current_fork_perc:0.00
 * current_save_keys_processed:0
 * current_save_keys_total:0
 * rdb_changes_since_last_save:0
 * rdb_bgsave_in_progress:0
 * rdb_last_save_time:1706057809
 * rdb_last_bgsave_status:ok
 * rdb_last_bgsave_time_sec:0
 * rdb_current_bgsave_time_sec:-1
 * rdb_last_cow_size:4513792
 * aof_enabled:0
 * aof_rewrite_in_progress:0
 * aof_rewrite_scheduled:0
 * aof_last_rewrite_time_sec:-1
 * aof_current_rewrite_time_sec:-1
 * aof_last_bgrewrite_status:ok
 * aof_last_write_status:ok
 * aof_last_cow_size:0
 * module_fork_in_progress:0
 * module_fork_last_cow_size:0
 * 
 * # Stats
 * total_connections_received:2
 * total_commands_processed:22619
 * instantaneous_ops_per_sec:1
 * total_net_input_bytes:851602
 * total_net_output_bytes:52209
 * instantaneous_input_kbps:0.04
 * instantaneous_output_kbps:0.00
 * rejected_connections:0
 * sync_full:1
 * sync_partial_ok:0
 * sync_partial_err:1
 * expired_keys:0
 * expired_stale_perc:0.00
 * expired_time_cap_reached_count:0
 * expire_cycle_cpu_milliseconds:263
 * evicted_keys:0
 * keyspace_hits:0
 * keyspace_misses:0
 * pubsub_channels:0
 * pubsub_patterns:0
 * latest_fork_usec:18835
 * total_forks:1
 * migrate_cached_sockets:0
 * slave_expires_tracked_keys:0
 * active_defrag_hits:0
 * active_defrag_misses:0
 * active_defrag_key_hits:0
 * active_defrag_key_misses:0
 * tracking_total_keys:0
 * tracking_total_items:0
 * tracking_total_prefixes:0
 * unexpected_error_replies:0
 * total_error_replies:0
 * dump_payload_sanitizations:0
 * total_reads_processed:22621
 * total_writes_processed:2266
 * io_threaded_reads_processed:0
 * io_threaded_writes_processed:0
 * 
 * # Replication
 * role:master
 * connected_slaves:1
 * slave0:ip=192.168.56.105,port=17005,state=online,offset=31654,lag=0
 * master_failover_state:no-failover
 * master_replid:1f652fa9b3c0d64414cd10c2592839f9b4bd03d3
 * master_replid2:0000000000000000000000000000000000000000
 * master_repl_offset:31654
 * second_repl_offset:-1
 * repl_backlog_active:1
 * repl_backlog_size:1048576
 * repl_backlog_first_byte_offset:1
 * repl_backlog_histlen:31654
 * 
 * # CPU
 * used_cpu_sys:10.172281
 * used_cpu_user:14.330245
 * used_cpu_sys_children:0.007970
 * used_cpu_user_children:0.003985
 * used_cpu_sys_main_thread:9.538480
 * used_cpu_user_main_thread:14.165628
 * 
 * # Modules
 * 
 * # Errorstats
 * 
 * # Cluster
 * cluster_enabled:1
 * 
 * # Keyspace
 * db0:keys=1,expires=0,avg_ttl=0
 * </pre>
 */
public class RedisInfo {
	private Server server;
	private Clients clients;
	private Memory memory;
	private Persistence persistence;
	private Replication replication;
	private CPU cpu;
	private Modules modules;
	private Errorstats errorstats;
	private Cluster cluster;
	private Keyspace keyspace;

	/**
	 * @return the server
	 */
	public Server server() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void server(Server server) {
		this.server = server;
	}

	/**
	 * @return the clients
	 */
	public Clients clients() {
		return clients;
	}

	/**
	 * @param clients the clients to set
	 */
	public void clients(Clients clients) {
		this.clients = clients;
	}

	/**
	 * @return the memory
	 */
	public Memory memory() {
		return memory;
	}

	/**
	 * @param memory the memory to set
	 */
	public void memory(Memory memory) {
		this.memory = memory;
	}

	/**
	 * @return the persistence
	 */
	public Persistence persistence() {
		return persistence;
	}

	/**
	 * @param persistence the persistence to set
	 */
	public void persistence(Persistence persistence) {
		this.persistence = persistence;
	}

	/**
	 * @return the replication
	 */
	public Replication replication() {
		return replication;
	}

	/**
	 * @param replication the replication to set
	 */
	public void replication(Replication replication) {
		this.replication = replication;
	}

	/**
	 * @return the cpu
	 */
	public CPU cpu() {
		return cpu;
	}

	/**
	 * @param cpu the cpu to set
	 */
	public void cpu(CPU cpu) {
		this.cpu = cpu;
	}

	/**
	 * @return the modules
	 */
	public Modules modules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void modules(Modules modules) {
		this.modules = modules;
	}

	/**
	 * @return the errorstats
	 */
	public Errorstats errorstats() {
		return errorstats;
	}

	/**
	 * @param errorstats the errorstats to set
	 */
	public void errorstats(Errorstats errorstats) {
		this.errorstats = errorstats;
	}

	/**
	 * @return the cluster
	 */
	public Cluster cluster() {
		return cluster;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void cluster(Cluster cluster) {
		this.cluster = cluster;
	}

	/**
	 * @return the keyspace
	 */
	public Keyspace keyspace() {
		return keyspace;
	}

	/**
	 * @param keyspace the keyspace to set
	 */
	public void keyspace(Keyspace keyspace) {
		this.keyspace = keyspace;
	}

	/**
	 * <pre>
	 * redisVersion:6.2.14
	 * redisGitSha1:00000000
	 * redisGitDirty:0
	 * redisBuildId:a712fce3205cb7ee
	 * redisMode:cluster
	 * os:linux 3.10.0-1160.105.1.el7.x8664 x8664
	 * archBits:64
	 * monotonicClock:posix clockGettime
	 * multiplexingApi:epoll
	 * atomicvarApi:atomic-builtin
	 * gccVersion:4.8.5
	 * processId:11876
	 * processSupervised:no
	 * runId:e8505ab372759c6c433f8b366eb9d923087d44c9
	 * tcpPort:17001
	 * serverTimeUsec:1706250770102856
	 * uptimeInSeconds:18181
	 * uptimeInDays:0
	 * hz:10
	 * configuredHz:10
	 * lruClock:11751954
	 * executable:/data/redis/cluster/node1/./redis-server
	 * configFile:/data/redis/cluster/node1/./redis.conf
	 * ioThreadsActive:0
	 * 
	 * </pre>
	 */
	class Server {
		private String redisVersion;
		private String redisGitSha1;
		private int redisGitDirty;
		private String redisBuildId;
		private String redisMode;
		private String os;
		private String archBits;
		private String monotonicClock;
		private String multiplexingApi;
		private String atomicvarApi;
		private String gccVersion;
		private int processId;
		private String processSupervised;
		private String runId;
		private int tcpPort;
		private long serverTimeUsec;
		private int uptimeInSeconds;
		private int uptimeInDays;
		private int hz;
		private int configuredHz;
		private long lruClock;
		private String executable;
		private String configFile;
		private int ioThreadsActive;

		// Constructor
		Server() {
		}

		// Redis Version
		public String redisVersion() {
			return redisVersion;
		}

		public void redisVersion(String redisVersion) {
			this.redisVersion = redisVersion;
		}

		// Redis Git SHA1
		public String redisGitSha1() {
			return redisGitSha1;
		}

		public void redisGitSha1(String redisGitSha1) {
			this.redisGitSha1 = redisGitSha1;
		}

		// Redis Git Dirty
		public int redisGitDirty() {
			return redisGitDirty;
		}

		public void redisGitDirty(int redisGitDirty) {
			this.redisGitDirty = redisGitDirty;
		}

		// Redis Build ID
		public String redisBuildId() {
			return redisBuildId;
		}

		public void redisBuildId(String redisBuildId) {
			this.redisBuildId = redisBuildId;
		}

		// Redis Mode
		public String redisMode() {
			return redisMode;
		}

		public void redisMode(String redisMode) {
			this.redisMode = redisMode;
		}

		// OS
		public String os() {
			return os;
		}

		public void os(String os) {
			this.os = os;
		}

		// Arch Bits
		public String archBits() {
			return archBits;
		}

		public void archBits(String archBits) {
			this.archBits = archBits;
		}

		// Monotonic Clock
		public String monotonicClock() {
			return monotonicClock;
		}

		public void monotonicClock(String monotonicClock) {
			this.monotonicClock = monotonicClock;
		}

		// Multiplexing API
		public String multiplexingApi() {
			return multiplexingApi;
		}

		public void multiplexingApi(String multiplexingApi) {
			this.multiplexingApi = multiplexingApi;
		}

		// AtomicVar API
		public String atomicvarApi() {
			return atomicvarApi;
		}

		public void atomicvarApi(String atomicvarApi) {
			this.atomicvarApi = atomicvarApi;
		}

		// GCC Version
		public String gccVersion() {
			return gccVersion;
		}

		public void gccVersion(String gccVersion) {
			this.gccVersion = gccVersion;
		}

		// Process ID
		public int processId() {
			return processId;
		}

		public void processId(int processId) {
			this.processId = processId;
		}

		// Process Supervised
		public String processSupervised() {
			return processSupervised;
		}

		public void processSupervised(String processSupervised) {
			this.processSupervised = processSupervised;
		}

		// Run ID
		public String runId() {
			return runId;
		}

		public void runId(String runId) {
			this.runId = runId;
		}

		// TCP Port
		public int tcpPort() {
			return tcpPort;
		}

		public void tcpPort(int tcpPort) {
			this.tcpPort = tcpPort;
		}

		// Server Time Microseconds
		public long serverTimeUsec() {
			return serverTimeUsec;
		}

		public void serverTimeUsec(long serverTimeUsec) {
			this.serverTimeUsec = serverTimeUsec;
		}

		// Uptime in Seconds
		public int uptimeInSeconds() {
			return uptimeInSeconds;
		}

		public void uptimeInSeconds(int uptimeInSeconds) {
			this.uptimeInSeconds = uptimeInSeconds;
		}

		// Uptime in Days
		public int uptimeInDays() {
			return uptimeInDays;
		}

		public void uptimeInDays(int uptimeInDays) {
			this.uptimeInDays = uptimeInDays;
		}

		// HZ
		public int hz() {
			return hz;
		}

		public void hz(int hz) {
			this.hz = hz;
		}

		// Configured HZ
		public int configuredHz() {
			return configuredHz;
		}

		public void configuredHz(int configuredHz) {
			this.configuredHz = configuredHz;
		}

		// LRU Clock
		public long lruClock() {
			return lruClock;
		}

		public void lruClock(long lruClock) {
			this.lruClock = lruClock;
		}

		// Executable
		public String executable() {
			return executable;
		}

		public void executable(String executable) {
			this.executable = executable;
		}

		// Config File
		public String configFile() {
			return configFile;
		}

		public void configFile(String configFile) {
			this.configFile = configFile;
		}

		// IO Threads Active
		public int ioThreadsActive() {
			return ioThreadsActive;
		}

		public void ioThreadsActive(int ioThreadsActive) {
			this.ioThreadsActive = ioThreadsActive;
		}

		public void fromString(String infoString) {
			String[] lines = infoString.split("\n");
			Map<String, String> infoMap = new HashMap<>();

			for (String line : lines) {
				String[] parts = line.split(":");
				if (parts.length == 2) {
					String key = parts[0].trim();
					String value = parts[1].trim();
					infoMap.put(key, value);
				}
			}

			// Set values in the Server object
			this.redisVersion(infoMap.get("redis_version"));
			this.redisGitSha1(infoMap.get("redis_git_sha1"));
			this.redisGitDirty(Integer.parseInt(infoMap.get("redis_git_dirty")));
			this.redisBuildId(infoMap.get("redis_build_id"));
			this.redisMode(infoMap.get("redis_mode"));
			this.os(infoMap.get("os"));
			this.archBits(infoMap.get("arch_bits"));
			this.monotonicClock(infoMap.get("monotonic_clock"));
			this.multiplexingApi(infoMap.get("multiplexing_api"));
			this.atomicvarApi(infoMap.get("atomicvar_api"));
			this.gccVersion(infoMap.get("gcc_version"));
			this.processId(Integer.parseInt(infoMap.get("process_id")));
			this.processSupervised(infoMap.get("process_supervised"));
			this.runId(infoMap.get("run_id"));
			this.tcpPort(Integer.parseInt(infoMap.get("tcp_port")));
			this.serverTimeUsec(Long.parseLong(infoMap.get("server_time_usec")));
			this.uptimeInSeconds(Integer.parseInt(infoMap.get("uptime_in_seconds")));
			this.uptimeInDays(Integer.parseInt(infoMap.get("uptime_in_days")));
			this.hz(Integer.parseInt(infoMap.get("hz")));
			this.configuredHz(Integer.parseInt(infoMap.get("configured_hz")));
			this.lruClock(Long.parseLong(infoMap.get("lru_clock")));
			this.executable(infoMap.get("executable"));
			this.configFile(infoMap.get("config_file"));
			this.ioThreadsActive(Integer.parseInt(infoMap.get("io_threads_active")));
		}
	}

	final class Clients {
		private int connectedClients;
		private int clusterConnections;
		private int maxClients;
		private int clientRecentMaxInputBuffer;
		private int clientRecentMaxOutputBuffer;
		private int blockedClients;
		private int trackingClients;
		private int clientsInTimeoutTable;

		// Constructor
		public Clients() {
		}

		// Getter and Setter methods
		public int connectedClients() {
			return connectedClients;
		}

		public void connectedClients(int connectedClients) {
			this.connectedClients = connectedClients;
		}

		public int clusterConnections() {
			return clusterConnections;
		}

		public void clusterConnections(int clusterConnections) {
			this.clusterConnections = clusterConnections;
		}

		public int maxClients() {
			return maxClients;
		}

		public void maxClients(int maxClients) {
			this.maxClients = maxClients;
		}

		public int clientRecentMaxInputBuffer() {
			return clientRecentMaxInputBuffer;
		}

		public void clientRecentMaxInputBuffer(int clientRecentMaxInputBuffer) {
			this.clientRecentMaxInputBuffer = clientRecentMaxInputBuffer;
		}

		public int clientRecentMaxOutputBuffer() {
			return clientRecentMaxOutputBuffer;
		}

		public void clientRecentMaxOutputBuffer(int clientRecentMaxOutputBuffer) {
			this.clientRecentMaxOutputBuffer = clientRecentMaxOutputBuffer;
		}

		public int blockedClients() {
			return blockedClients;
		}

		public void blockedClients(int blockedClients) {
			this.blockedClients = blockedClients;
		}

		public int trackingClients() {
			return trackingClients;
		}

		public void trackingClients(int trackingClients) {
			this.trackingClients = trackingClients;
		}

		public int clientsInTimeoutTable() {
			return clientsInTimeoutTable;
		}

		public void clientsInTimeoutTable(int clientsInTimeoutTable) {
			this.clientsInTimeoutTable = clientsInTimeoutTable;
		}

		public Clients fromString(String infoString) {
			Clients clients = new Clients();
			Map<String, String> infoMap = parseInfoString(infoString);

			for (Map.Entry<String, String> entry : infoMap.entrySet()) {
				setField(clients, entry.getKey(), entry.getValue());
			}

			return clients;
		}

		// Private method to parse info string into a map
		private Map<String, String> parseInfoString(String infoString) {
			Map<String, String> infoMap = new HashMap<>();
			String[] lines = infoString.split("\n");

			for (String line : lines) {
				String[] parts = line.split(":");
				if (parts.length == 2) {
					String key = parts[0].trim();
					String value = parts[1].trim();
					infoMap.put(key, value);
				}
			}

			return infoMap;
		}

		// Private method to set field values using reflection
		private void setField(Clients clients, String fieldName, String value) {
			try {
				Field field = Clients.class.getDeclaredField(fieldName);
				field.setAccessible(true);

				if (field.getType() == int.class) {
					field.setInt(clients, Integer.parseInt(value));
				}
			} catch (NoSuchFieldException | IllegalAccessException | NumberFormatException e) {
				e.printStackTrace(); // Handle the exception appropriately
			}
		}
	}

	final class Memory {

	}

	final class Persistence {

	}

	final class Replication {

	}

	final class CPU {

	}

	final class Modules {

	}

	final class Errorstats {

	}

	final class Cluster {

	}

	final class Keyspace {

	}
}
