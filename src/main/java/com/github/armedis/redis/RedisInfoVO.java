package com.github.armedis.redis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class RedisInfoVO {
	@Override
	public String toString() {
		return "RedisInfoVO [serverInfo=" + serverInfo + ", clientsInfo=" + clientsInfo + "]";
	}

	private ServerInfo serverInfo;
	private ClientsInfo clientsInfo;
	// Add more sections as needed

	// Constructor, getters, and setters go here...

	// Static method to create an instance of RedisInfoVO from the INFO command
	// result
	public static RedisInfoVO fromInfoCommandResult(String infoResult) {
		RedisInfoVO redisInfoVO = new RedisInfoVO();
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
					redisInfoVO.setServerInfo(ServerInfo.fromString(sectionContent));
					break;
				case "Clients":
					redisInfoVO.setClientsInfo(ClientsInfo.fromString(sectionContent));
					break;
				// Add more cases for other sections as needed
				}
			}
		}

		return redisInfoVO;
	}

	/**
	 * @return the serverInfo
	 */
	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	/**
	 * @param serverInfo the serverInfo to set
	 */
	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}

	/**
	 * @return the clientsInfo
	 */
	public ClientsInfo getClientsInfo() {
		return clientsInfo;
	}

	/**
	 * @param clientsInfo the clientsInfo to set
	 */
	public void setClientsInfo(ClientsInfo clientsInfo) {
		this.clientsInfo = clientsInfo;
	}

	public static class ServerInfo {
		@Override
		public String toString() {
			return "ServerInfo [redisVersion=" + redisVersion + ", redisGitSha1=" + redisGitSha1 + "]";
		}

		private String redisVersion;

		private String redisGitSha1;
		// Add more fields as needed

		/**
		 * @return the redisVersion
		 */
		public String getRedisVersion() {
			return redisVersion;
		}

		/**
		 * @param redisVersion the redisVersion to set
		 */
		public void setRedisVersion(String redisVersion) {
			this.redisVersion = redisVersion;
		}

		/**
		 * @return the redisGitSha1
		 */
		public String getRedisGitSha1() {
			return redisGitSha1;
		}

		/**
		 * @param redisGitSha1 the redisGitSha1 to set
		 */
		public void setRedisGitSha1(String redisGitSha1) {
			this.redisGitSha1 = redisGitSha1;
		}

		// Constructor, getters, and setters for ServerInfo go here...

		public static ServerInfo fromString(String content) {
			ServerInfo serverInfo = new ServerInfo();
			String[] lines = content.split("\r\n");

			for (String line : lines) {
				String[] parts = line.split(":");
				if (parts.length == 2) {
					String key = parts[0].trim();
					String value = parts[1].trim();

					switch (key) {
					case "redis_version":
						serverInfo.setRedisVersion(value);
						break;
					case "redis_git_sha1":
						serverInfo.setRedisGitSha1(value);
						break;
					// Add more cases for other fields in ServerInfo as needed
					}
				}
			}

			return serverInfo;
		}
	}

	public static class ClientsInfo {
		@Override
		public String toString() {
			return "ClientsInfo [connectedClients=" + connectedClients + ", blockedClients=" + blockedClients + "]";
		}

		private int connectedClients;

		private int blockedClients;
		// Add more fields as needed

		/**
		 * @return the connectedClients
		 */
		public int getConnectedClients() {
			return connectedClients;
		}

		/**
		 * @param connectedClients the connectedClients to set
		 */
		public void setConnectedClients(int connectedClients) {
			this.connectedClients = connectedClients;
		}

		/**
		 * @return the blockedClients
		 */
		public int getBlockedClients() {
			return blockedClients;
		}

		/**
		 * @param blockedClients the blockedClients to set
		 */
		public void setBlockedClients(int blockedClients) {
			this.blockedClients = blockedClients;
		}

		// Constructor, getters, and setters for ClientsInfo go here...

		public static ClientsInfo fromString(String content) {
			ClientsInfo clientsInfo = new ClientsInfo();
			String[] lines = content.split("\r\n");

			for (String line : lines) {
				String[] parts = line.split(":");
				if (parts.length == 2) {
					String key = parts[0].trim();
					String value = parts[1].trim();

					switch (key) {
					case "connected_clients":
						clientsInfo.setConnectedClients(Integer.parseInt(value));
						break;
					case "blocked_clients":
						clientsInfo.setBlockedClients(Integer.parseInt(value));
						break;
					// Add more cases for other fields in ClientsInfo as needed
					}
				}
			}

			return clientsInfo;
		}
	}

	// Add more internal classes for other sections as needed

	// Example usage
	public static void main(String[] args) {
		String infoResult = "# Server\r\n" + "redis_version:6.2.14\r\n" + "redis_git_sha1:00000000\r\n"
				+ "redis_git_dirty:0\r\n" + "redis_build_id:a712fce3205cb7ee\r\n" + "redis_mode:cluster\r\n"
				+ "os:Linux 3.10.0-1160.105.1.el7.x86_64 x86_64\r\n" + "arch_bits:64\r\n"
				+ "monotonic_clock:POSIX clock_gettime\r\n" + "multiplexing_api:epoll\r\n"
				+ "atomicvar_api:atomic-builtin\r\n" + "gcc_version:4.8.5\r\n" + "process_id:11876\r\n"
				+ "process_supervised:no\r\n" + "run_id:e8505ab372759c6c433f8b366eb9d923087d44c9\r\n"
				+ "tcp_port:17001\r\n" + "server_time_usec:1706253637142309\r\n" + "uptime_in_seconds:21048\r\n"
				+ "uptime_in_days:0\r\n" + "hz:10\r\n" + "configured_hz:10\r\n" + "lru_clock:11754821\r\n"
				+ "executable:/data/redis/cluster/node1/./redis-server\r\n"
				+ "config_file:/data/redis/cluster/node1/./redis.conf\r\n" + "io_threads_active:0\r\n" + "\r\n"
				+ "# Clients\r\n" + "connected_clients:1\r\n" + "cluster_connections:10\r\n" + "maxclients:4064\r\n"
				+ "client_recent_max_input_buffer:32\r\n" + "client_recent_max_output_buffer:0\r\n"
				+ "blocked_clients:0\r\n" + "tracking_clients:0\r\n" + "clients_in_timeout_table:0\r\n" + "\r\n"
				+ "# Memory\r\n" + "used_memory:2406744\r\n" + "used_memory_human:2.30M\r\n"
				+ "used_memory_rss:12894208\r\n" + "used_memory_rss_human:12.30M\r\n" + "used_memory_peak:2572664\r\n"
				+ "used_memory_peak_human:2.45M\r\n" + "used_memory_peak_perc:93.55%\r\n"
				+ "used_memory_overhead:2294896\r\n" + "used_memory_startup:1205192\r\n"
				+ "used_memory_dataset:111848\r\n" + "used_memory_dataset_perc:9.31%\r\n"
				+ "allocator_allocated:2454248\r\n" + "allocator_active:2760704\r\n" + "allocator_resident:5279744\r\n"
				+ "total_system_memory:4388077568\r\n" + "total_system_memory_human:4.09G\r\n"
				+ "used_memory_lua:30720\r\n" + "used_memory_lua_human:30.00K\r\n" + "used_memory_scripts:0\r\n"
				+ "used_memory_scripts_human:0B\r\n" + "number_of_cached_scripts:0\r\n" + "maxmemory:0\r\n"
				+ "maxmemory_human:0B\r\n" + "maxmemory_policy:noeviction\r\n" + "allocator_frag_ratio:1.12\r\n"
				+ "allocator_frag_bytes:306456\r\n" + "allocator_rss_ratio:1.91\r\n" + "allocator_rss_bytes:2519040\r\n"
				+ "rss_overhead_ratio:2.44\r\n" + "rss_overhead_bytes:7614464\r\n" + "mem_fragmentation_ratio:5.36\r\n"
				+ "mem_fragmentation_bytes:10489264\r\n" + "mem_not_counted_for_evict:0\r\n"
				+ "mem_replication_backlog:1048576\r\n" + "mem_clients_slaves:20512\r\n"
				+ "mem_clients_normal:20512\r\n" + "mem_aof_buffer:0\r\n" + "mem_allocator:jemalloc-5.1.0\r\n"
				+ "active_defrag_running:0\r\n" + "lazyfree_pending_objects:0\r\n" + "lazyfreed_objects:0\r\n" + "\r\n"
				+ "# Persistence\r\n" + "loading:0\r\n" + "current_cow_size:0\r\n" + "current_cow_size_age:0\r\n"
				+ "current_fork_perc:0.00\r\n" + "current_save_keys_processed:0\r\n" + "current_save_keys_total:0\r\n"
				+ "rdb_changes_since_last_save:0\r\n" + "rdb_bgsave_in_progress:0\r\n"
				+ "rdb_last_save_time:1706232590\r\n" + "rdb_last_bgsave_status:ok\r\n"
				+ "rdb_last_bgsave_time_sec:1\r\n" + "rdb_current_bgsave_time_sec:-1\r\n"
				+ "rdb_last_cow_size:6520832\r\n" + "aof_enabled:0\r\n" + "aof_rewrite_in_progress:0\r\n"
				+ "aof_rewrite_scheduled:0\r\n" + "aof_last_rewrite_time_sec:-1\r\n"
				+ "aof_current_rewrite_time_sec:-1\r\n" + "aof_last_bgrewrite_status:ok\r\n"
				+ "aof_last_write_status:ok\r\n" + "aof_last_cow_size:0\r\n" + "module_fork_in_progress:0\r\n"
				+ "module_fork_last_cow_size:0\r\n" + "\r\n" + "# Stats\r\n" + "total_connections_received:769\r\n"
				+ "total_commands_processed:34842\r\n" + "instantaneous_ops_per_sec:1\r\n"
				+ "total_net_input_bytes:1022057\r\n" + "total_net_output_bytes:17445006\r\n"
				+ "instantaneous_input_kbps:0.05\r\n" + "instantaneous_output_kbps:0.00\r\n"
				+ "rejected_connections:0\r\n" + "sync_full:1\r\n" + "sync_partial_ok:0\r\n" + "sync_partial_err:1\r\n"
				+ "expired_keys:0\r\n" + "expired_stale_perc:0.00\r\n" + "expired_time_cap_reached_count:0\r\n"
				+ "expire_cycle_cpu_milliseconds:237\r\n" + "evicted_keys:0\r\n" + "keyspace_hits:8\r\n"
				+ "keyspace_misses:0\r\n" + "pubsub_channels:0\r\n" + "pubsub_patterns:0\r\n"
				+ "latest_fork_usec:17298\r\n" + "total_forks:1\r\n" + "migrate_cached_sockets:0\r\n"
				+ "slave_expires_tracked_keys:0\r\n" + "active_defrag_hits:0\r\n" + "active_defrag_misses:0\r\n"
				+ "active_defrag_key_hits:0\r\n" + "active_defrag_key_misses:0\r\n" + "tracking_total_keys:0\r\n"
				+ "tracking_total_items:0\r\n" + "tracking_total_prefixes:0\r\n" + "unexpected_error_replies:0\r\n"
				+ "total_error_replies:8\r\n" + "dump_payload_sanitizations:0\r\n" + "total_reads_processed:35511\r\n"
				+ "total_writes_processed:16721\r\n" + "io_threaded_reads_processed:0\r\n"
				+ "io_threaded_writes_processed:0\r\n" + "\r\n" + "# Replication\r\n" + "role:master\r\n"
				+ "connected_slaves:1\r\n" + "slave0:ip=192.168.56.105,port=17005,state=online,offset=28028,lag=0\r\n"
				+ "master_failover_state:no-failover\r\n" + "master_replid:e8d3aa2441fbf53d2766e01ffe32262b15efcc3e\r\n"
				+ "master_replid2:0000000000000000000000000000000000000000\r\n" + "master_repl_offset:28028\r\n"
				+ "second_repl_offset:-1\r\n" + "repl_backlog_active:1\r\n" + "repl_backlog_size:1048576\r\n"
				+ "repl_backlog_first_byte_offset:1\r\n" + "repl_backlog_histlen:28028\r\n" + "\r\n" + "# CPU\r\n"
				+ "used_cpu_sys:12.553367\r\n" + "used_cpu_user:17.318401\r\n" + "used_cpu_sys_children:0.018534\r\n"
				+ "used_cpu_user_children:0.000000\r\n" + "used_cpu_sys_main_thread:11.817680\r\n"
				+ "used_cpu_user_main_thread:17.130294\r\n" + "\r\n" + "# Modules\r\n" + "\r\n" + "# Errorstats\r\n"
				+ "errorstat_ERR:count=8\r\n" + "\r\n" + "# Cluster\r\n" + "cluster_enabled:1\r\n" + "\r\n"
				+ "# Keyspace\r\n" + "db0:keys=1,expires=0,avg_ttl=0\r\n" + "";
		RedisInfoVO redisInfoVO = RedisInfoVO.fromInfoCommandResult(infoResult);
		// Use the parsed information as needed
		
		redisInfoVO.ser
		System.out.println(redisInfoVO);
	}
}