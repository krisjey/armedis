package com.github.armedis.redis;

import java.util.ArrayList;
import java.util.List;

public class RedisServerStatus {

    private String redisVersion;
    private String redisGitSha1;
    private int processId;
    private String runId;
    private int tcpPort;
    private long serverTimeUsec;
    private long uptimeInSeconds;

    private int connectedClients;
    private int clusterConnections;
    private int maxClients;
    private int blockedClients;

    private long usedMemory;
    private String usedMemoryHuman;
    private long usedMemoryRss;
    private String usedMemoryRssHuman;
    private long usedMemoryPeak;
    private String usedMemoryPeakHuman;
    private double usedMemoryPeakPerc;

    private int loading;
    private int rdbChangesSinceLastSave;
    private int rdbLastSaveTime;
    private String rdbLastBgsaveStatus;

    private int totalConnectionsReceived;
    private int totalCommandsProcessed;
    private int instantaneousOpsPerSec;
    private long totalNetInputBytes;
    private long totalNetOutputBytes;

    private String role;
    private int connectedSlaves;
    private List<SlaveInfo> slaves;

    private double usedCpuSys;
    private double usedCpuUser;

    private int clusterEnabled;

    private int keysInDatabase;
    private int expiresInDatabase;
    private int avgTtlInDatabase;

    public RedisServerStatus() {
        this.slaves = new ArrayList<>();
    }

    public void setRedisVersion(String redisVersion) {
        this.redisVersion = redisVersion;
    }

    public void setRedisGitSha1(String redisGitSha1) {
        this.redisGitSha1 = redisGitSha1;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public void setServerTimeUsec(long serverTimeUsec) {
        this.serverTimeUsec = serverTimeUsec;
    }

    public void setUptimeInSeconds(long uptimeInSeconds) {
        this.uptimeInSeconds = uptimeInSeconds;
    }

    public void setConnectedClients(int connectedClients) {
        this.connectedClients = connectedClients;
    }

    public void setClusterConnections(int clusterConnections) {
        this.clusterConnections = clusterConnections;
    }

    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    public void setBlockedClients(int blockedClients) {
        this.blockedClients = blockedClients;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public void setUsedMemoryHuman(String usedMemoryHuman) {
        this.usedMemoryHuman = usedMemoryHuman;
    }

    public void setUsedMemoryRss(long usedMemoryRss) {
        this.usedMemoryRss = usedMemoryRss;
    }

    public void setUsedMemoryRssHuman(String usedMemoryRssHuman) {
        this.usedMemoryRssHuman = usedMemoryRssHuman;
    }

    public void setUsedMemoryPeak(long usedMemoryPeak) {
        this.usedMemoryPeak = usedMemoryPeak;
    }

    public void setUsedMemoryPeakHuman(String usedMemoryPeakHuman) {
        this.usedMemoryPeakHuman = usedMemoryPeakHuman;
    }

    public void setUsedMemoryPeakPerc(double usedMemoryPeakPerc) {
        this.usedMemoryPeakPerc = usedMemoryPeakPerc;
    }

    public void setLoading(int loading) {
        this.loading = loading;
    }

    public void setRdbChangesSinceLastSave(int rdbChangesSinceLastSave) {
        this.rdbChangesSinceLastSave = rdbChangesSinceLastSave;
    }

    public void setRdbLastSaveTime(int rdbLastSaveTime) {
        this.rdbLastSaveTime = rdbLastSaveTime;
    }

    public void setRdbLastBgsaveStatus(String rdbLastBgsaveStatus) {
        this.rdbLastBgsaveStatus = rdbLastBgsaveStatus;
    }

    public void setTotalConnectionsReceived(int totalConnectionsReceived) {
        this.totalConnectionsReceived = totalConnectionsReceived;
    }

    public void setTotalCommandsProcessed(int totalCommandsProcessed) {
        this.totalCommandsProcessed = totalCommandsProcessed;
    }

    public void setInstantaneousOpsPerSec(int instantaneousOpsPerSec) {
        this.instantaneousOpsPerSec = instantaneousOpsPerSec;
    }

    public void setTotalNetInputBytes(long totalNetInputBytes) {
        this.totalNetInputBytes = totalNetInputBytes;
    }

    public void setTotalNetOutputBytes(long totalNetOutputBytes) {
        this.totalNetOutputBytes = totalNetOutputBytes;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setConnectedSlaves(int connectedSlaves) {
        this.connectedSlaves = connectedSlaves;
    }

    public void addSlave(SlaveInfo slave) {
        this.slaves.add(slave);
    }

    public void setUsedCpuSys(double usedCpuSys) {
        this.usedCpuSys = usedCpuSys;
    }

    public void setUsedCpuUser(double usedCpuUser) {
        this.usedCpuUser = usedCpuUser;
    }

    public void setClusterEnabled(int clusterEnabled) {
        this.clusterEnabled = clusterEnabled;
    }

    public void setKeysInDatabase(int keysInDatabase) {
        this.keysInDatabase = keysInDatabase;
    }

    public void setExpiresInDatabase(int expiresInDatabase) {
        this.expiresInDatabase = expiresInDatabase;
    }

    public void setAvgTtlInDatabase(int avgTtlInDatabase) {
        this.avgTtlInDatabase = avgTtlInDatabase;
    }

    @Override
    public String toString() {
        return "RedisServerStatus{" +
                "redisVersion='" + redisVersion + '\'' +
                ", redisGitSha1='" + redisGitSha1 + '\'' +
                ", processId=" + processId +
                ", runId='" + runId + '\'' +
                ", tcpPort=" + tcpPort +
                ", serverTimeUsec=" + serverTimeUsec +
                ", uptimeInSeconds=" + uptimeInSeconds +
                ", connectedClients=" + connectedClients +
                ", clusterConnections=" + clusterConnections +
                ", maxClients=" + maxClients +
                ", blockedClients=" + blockedClients +
                ", usedMemory=" + usedMemory +
                ", usedMemoryHuman='" + usedMemoryHuman + '\'' +
                ", usedMemoryRss=" + usedMemoryRss +
                ", usedMemoryRssHuman='" + usedMemoryRssHuman + '\'' +
                ", usedMemoryPeak=" + usedMemoryPeak +
                ", usedMemoryPeakHuman='" + usedMemoryPeakHuman + '\'' +
                ", usedMemoryPeakPerc=" + usedMemoryPeakPerc +
                ", loading=" + loading +
                ", rdbChangesSinceLastSave=" + rdbChangesSinceLastSave +
                ", rdbLastSaveTime=" + rdbLastSaveTime +
                ", rdbLastBgsaveStatus='" + rdbLastBgsaveStatus + '\'' +
                ", totalConnectionsReceived=" + totalConnectionsReceived +
                ", totalCommandsProcessed=" + totalCommandsProcessed +
                ", instantaneousOpsPerSec=" + instantaneousOpsPerSec +
                ", totalNetInputBytes=" + totalNetInputBytes +
                ", totalNetOutputBytes=" + totalNetOutputBytes +
                ", role='" + role + '\'' +
                ", connectedSlaves=" + connectedSlaves +
                ", slaves=" + slaves +
                ", usedCpuSys=" + usedCpuSys +
                ", usedCpuUser=" + usedCpuUser +
                ", clusterEnabled=" + clusterEnabled +
                ", keysInDatabase=" + keysInDatabase +
                ", expiresInDatabase=" + expiresInDatabase +
                ", avgTtlInDatabase=" + avgTtlInDatabase +
                '}';
    }

    public static class SlaveInfo {
        private String ip;
        private int port;
        private String state;
        /**
		 * @return the ip
		 */
		public String getIp() {
			return ip;
		}

		/**
		 * @param ip the ip to set
		 */
		public void setIp(String ip) {
			this.ip = ip;
		}

		/**
		 * @return the port
		 */
		public int getPort() {
			return port;
		}

		/**
		 * @param port the port to set
		 */
		public void setPort(int port) {
			this.port = port;
		}

		/**
		 * @return the state
		 */
		public String getState() {
			return state;
		}

		/**
		 * @param state the state to set
		 */
		public void setState(String state) {
			this.state = state;
		}

		/**
		 * @return the offset
		 */
		public long getOffset() {
			return offset;
		}

		/**
		 * @param offset the offset to set
		 */
		public void setOffset(long offset) {
			this.offset = offset;
		}

		/**
		 * @return the lag
		 */
		public int getLag() {
			return lag;
		}

		/**
		 * @param lag the lag to set
		 */
		public void setLag(int lag) {
			this.lag = lag;
		}

		private long offset;
        private int lag;

        // Getters and setters for SlaveInfo go here...

        @Override
        public String toString() {
            return "SlaveInfo{" +
                    "ip='" + ip + '\'' +
                    ", port=" + port +
                    ", state='" + state + '\'' +
                    ", offset=" + offset +
                    ", lag=" + lag +
                    '}';
        }
    }

    public static RedisServerStatus fromString(String input) {
        RedisServerStatus redisServerStatus = new RedisServerStatus();
        String[] lines = input.split("\n");

        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length >= 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                    case "redis_version":
                        redisServerStatus.setRedisVersion(value);
                        break;
                    case "redis_git_sha1":
                        redisServerStatus.setRedisGitSha1(value);
                        break;
                    case "process_id":
                        redisServerStatus.setProcessId(Integer.parseInt(value));
                        break;
                    case "run_id":
                        redisServerStatus.setRunId(value);
                        break;
                    case "tcp_port":
                        redisServerStatus.setTcpPort(Integer.parseInt(value));
                        break;
                    case "server_time_usec":
                        redisServerStatus.setServerTimeUsec(Long.parseLong(value));
                        break;
                    case "uptime_in_seconds":
                        redisServerStatus.setUptimeInSeconds(Long.parseLong(value));
                        break;
                    case "connected_clients":
                        redisServerStatus.setConnectedClients(Integer.parseInt(value));
                        break;
                    case "cluster_connections":
                        redisServerStatus.setClusterConnections(Integer.parseInt(value));
                        break;
                    case "maxclients":
                        redisServerStatus.setMaxClients(Integer.parseInt(value));
                        break;
                    case "blocked_clients":
                        redisServerStatus.setBlockedClients(Integer.parseInt(value));
                        break;
                    case "used_memory":
                        redisServerStatus.setUsedMemory(Long.parseLong(value));
                        break;
                    case "used_memory_human":
                        redisServerStatus.setUsedMemoryHuman(value);
                        break;
                    case "used_memory_rss":
                        redisServerStatus.setUsedMemoryRss(Long.parseLong(value));
                        break;
                    case "used_memory_rss_human":
                        redisServerStatus.setUsedMemoryRssHuman(value);
                        break;
                    case "used_memory_peak":
                        redisServerStatus.setUsedMemoryPeak(Long.parseLong(value));
                        break;
                    case "used_memory_peak_human":
                        redisServerStatus.setUsedMemoryPeakHuman(value);
                        break;
                    case "used_memory_peak_perc":
                        redisServerStatus.setUsedMemoryPeakPerc(Double.parseDouble(value.replace("%", "")));
                        break;
                    case "loading":
                        redisServerStatus.setLoading(Integer.parseInt(value));
                        break;
                    case "rdb_changes_since_last_save":
                        redisServerStatus.setRdbChangesSinceLastSave(Integer.parseInt(value));
                        break;
                    case "rdb_last_save_time":
                        redisServerStatus.setRdbLastSaveTime(Integer.parseInt(value));
                        break;
                    case "rdb_last_bgsave_status":
                        redisServerStatus.setRdbLastBgsaveStatus(value);
                        break;
                    case "total_connections_received":
                        redisServerStatus.setTotalConnectionsReceived(Integer.parseInt(value));
                        break;
                    case "total_commands_processed":
                        redisServerStatus.setTotalCommandsProcessed(Integer.parseInt(value));
                        break;
                    case "instantaneous_ops_per_sec":
                        redisServerStatus.setInstantaneousOpsPerSec(Integer.parseInt(value));
                        break;
                    case "total_net_input_bytes":
                        redisServerStatus.setTotalNetInputBytes(Long.parseLong(value));
                        break;
                    case "total_net_output_bytes":
                        redisServerStatus.setTotalNetOutputBytes(Long.parseLong(value));
                        break;
                    case "role":
                        redisServerStatus.setRole(value);
                        break;
                    case "connected_slaves":
                        redisServerStatus.setConnectedSlaves(Integer.parseInt(value));
                        break;
                    case "used_cpu_sys":
                        redisServerStatus.setUsedCpuSys(Double.parseDouble(value));
                        break;
                    case "used_cpu_user":
                        redisServerStatus.setUsedCpuUser(Double.parseDouble(value));
                        break;
                    case "cluster_enabled":
                        redisServerStatus.setClusterEnabled(Integer.parseInt(value));
                        break;
                    case "db0":
                        // Example: "db0:keys=1,expires=0,avg_ttl=0"
                        String[] dbInfo = value.split(",");
                        for (String dbPart : dbInfo) {
                            String[] dbKeyValue = dbPart.split("=");
                            if (dbKeyValue.length == 2) {
                                String dbKey = dbKeyValue[0].trim();
                                String dbValue = dbKeyValue[1].trim();
                                switch (dbKey) {
                                    case "keys":
                                        redisServerStatus.setKeysInDatabase(Integer.parseInt(dbValue));
                                        break;
                                    case "expires":
                                        redisServerStatus.setExpiresInDatabase(Integer.parseInt(dbValue));
                                        break;
                                    case "avg_ttl":
                                        redisServerStatus.setAvgTtlInDatabase(Integer.parseInt(dbValue));
                                        break;
                                }
                            }
                        }
                        break;
                    // Add more cases for other fields as needed
                }
            }
        }

        // Additional logic for parsing slave information
        for (String line : lines) {
            if (line.startsWith("slave")) {
                SlaveInfo slaveInfo = new SlaveInfo();
                String[] slaveParts = line.split(",");
                for (String part : slaveParts) {
                    String[] keyValue = part.split("=");
                    if (keyValue.length == 2) {
                        String slaveKey = keyValue[0].trim();
                        String slaveValue = keyValue[1].trim();
                        switch (slaveKey) {
                            case "ip":
                                slaveInfo.setIp(slaveValue);
                                break;
                            case "port":
                                slaveInfo.setPort(Integer.parseInt(slaveValue));
                                break;
                            case "state":
                                slaveInfo.setState(slaveValue);
                                break;
                            case "offset":
                                slaveInfo.setOffset(Long.parseLong(slaveValue));
                                break;
                            case "lag":
                                slaveInfo.setLag(Integer.parseInt(slaveValue));
                                break;
                        }
                    }
                }
                redisServerStatus.addSlave(slaveInfo);
            }
        }

        return redisServerStatus;
    }
//
//    // Example usage
//    public static void main(String[] args) {
//        String input = "# Server\r\n"
//        		+ "redis_version:6.2.14\r\n"
//        		+ "redis_git_sha1:00000000\r\n"
//        		+ "redis_git_dirty:0\r\n"
//        		+ "redis_build_id:a712fce3205cb7ee\r\n"
//        		+ "redis_mode:cluster\r\n"
//        		+ "os:Linux 3.10.0-1160.105.1.el7.x86_64 x86_64\r\n"
//        		+ "arch_bits:64\r\n"
//        		+ "monotonic_clock:POSIX clock_gettime\r\n"
//        		+ "multiplexing_api:epoll\r\n"
//        		+ "atomicvar_api:atomic-builtin\r\n"
//        		+ "gcc_version:4.8.5\r\n"
//        		+ "process_id:11876\r\n"
//        		+ "process_supervised:no\r\n"
//        		+ "run_id:e8505ab372759c6c433f8b366eb9d923087d44c9\r\n"
//        		+ "tcp_port:17001\r\n"
//        		+ "server_time_usec:1706253637142309\r\n"
//        		+ "uptime_in_seconds:21048\r\n"
//        		+ "uptime_in_days:0\r\n"
//        		+ "hz:10\r\n"
//        		+ "configured_hz:10\r\n"
//        		+ "lru_clock:11754821\r\n"
//        		+ "executable:/data/redis/cluster/node1/./redis-server\r\n"
//        		+ "config_file:/data/redis/cluster/node1/./redis.conf\r\n"
//        		+ "io_threads_active:0\r\n"
//        		+ "\r\n"
//        		+ "# Clients\r\n"
//        		+ "connected_clients:1\r\n"
//        		+ "cluster_connections:10\r\n"
//        		+ "maxclients:4064\r\n"
//        		+ "client_recent_max_input_buffer:32\r\n"
//        		+ "client_recent_max_output_buffer:0\r\n"
//        		+ "blocked_clients:0\r\n"
//        		+ "tracking_clients:0\r\n"
//        		+ "clients_in_timeout_table:0\r\n"
//        		+ "\r\n"
//        		+ "# Memory\r\n"
//        		+ "used_memory:2406744\r\n"
//        		+ "used_memory_human:2.30M\r\n"
//        		+ "used_memory_rss:12894208\r\n"
//        		+ "used_memory_rss_human:12.30M\r\n"
//        		+ "used_memory_peak:2572664\r\n"
//        		+ "used_memory_peak_human:2.45M\r\n"
//        		+ "used_memory_peak_perc:93.55%\r\n"
//        		+ "used_memory_overhead:2294896\r\n"
//        		+ "used_memory_startup:1205192\r\n"
//        		+ "used_memory_dataset:111848\r\n"
//        		+ "used_memory_dataset_perc:9.31%\r\n"
//        		+ "allocator_allocated:2454248\r\n"
//        		+ "allocator_active:2760704\r\n"
//        		+ "allocator_resident:5279744\r\n"
//        		+ "total_system_memory:4388077568\r\n"
//        		+ "total_system_memory_human:4.09G\r\n"
//        		+ "used_memory_lua:30720\r\n"
//        		+ "used_memory_lua_human:30.00K\r\n"
//        		+ "used_memory_scripts:0\r\n"
//        		+ "used_memory_scripts_human:0B\r\n"
//        		+ "number_of_cached_scripts:0\r\n"
//        		+ "maxmemory:0\r\n"
//        		+ "maxmemory_human:0B\r\n"
//        		+ "maxmemory_policy:noeviction\r\n"
//        		+ "allocator_frag_ratio:1.12\r\n"
//        		+ "allocator_frag_bytes:306456\r\n"
//        		+ "allocator_rss_ratio:1.91\r\n"
//        		+ "allocator_rss_bytes:2519040\r\n"
//        		+ "rss_overhead_ratio:2.44\r\n"
//        		+ "rss_overhead_bytes:7614464\r\n"
//        		+ "mem_fragmentation_ratio:5.36\r\n"
//        		+ "mem_fragmentation_bytes:10489264\r\n"
//        		+ "mem_not_counted_for_evict:0\r\n"
//        		+ "mem_replication_backlog:1048576\r\n"
//        		+ "mem_clients_slaves:20512\r\n"
//        		+ "mem_clients_normal:20512\r\n"
//        		+ "mem_aof_buffer:0\r\n"
//        		+ "mem_allocator:jemalloc-5.1.0\r\n"
//        		+ "active_defrag_running:0\r\n"
//        		+ "lazyfree_pending_objects:0\r\n"
//        		+ "lazyfreed_objects:0\r\n"
//        		+ "\r\n"
//        		+ "# Persistence\r\n"
//        		+ "loading:0\r\n"
//        		+ "current_cow_size:0\r\n"
//        		+ "current_cow_size_age:0\r\n"
//        		+ "current_fork_perc:0.00\r\n"
//        		+ "current_save_keys_processed:0\r\n"
//        		+ "current_save_keys_total:0\r\n"
//        		+ "rdb_changes_since_last_save:0\r\n"
//        		+ "rdb_bgsave_in_progress:0\r\n"
//        		+ "rdb_last_save_time:1706232590\r\n"
//        		+ "rdb_last_bgsave_status:ok\r\n"
//        		+ "rdb_last_bgsave_time_sec:1\r\n"
//        		+ "rdb_current_bgsave_time_sec:-1\r\n"
//        		+ "rdb_last_cow_size:6520832\r\n"
//        		+ "aof_enabled:0\r\n"
//        		+ "aof_rewrite_in_progress:0\r\n"
//        		+ "aof_rewrite_scheduled:0\r\n"
//        		+ "aof_last_rewrite_time_sec:-1\r\n"
//        		+ "aof_current_rewrite_time_sec:-1\r\n"
//        		+ "aof_last_bgrewrite_status:ok\r\n"
//        		+ "aof_last_write_status:ok\r\n"
//        		+ "aof_last_cow_size:0\r\n"
//        		+ "module_fork_in_progress:0\r\n"
//        		+ "module_fork_last_cow_size:0\r\n"
//        		+ "\r\n"
//        		+ "# Stats\r\n"
//        		+ "total_connections_received:769\r\n"
//        		+ "total_commands_processed:34842\r\n"
//        		+ "instantaneous_ops_per_sec:1\r\n"
//        		+ "total_net_input_bytes:1022057\r\n"
//        		+ "total_net_output_bytes:17445006\r\n"
//        		+ "instantaneous_input_kbps:0.05\r\n"
//        		+ "instantaneous_output_kbps:0.00\r\n"
//        		+ "rejected_connections:0\r\n"
//        		+ "sync_full:1\r\n"
//        		+ "sync_partial_ok:0\r\n"
//        		+ "sync_partial_err:1\r\n"
//        		+ "expired_keys:0\r\n"
//        		+ "expired_stale_perc:0.00\r\n"
//        		+ "expired_time_cap_reached_count:0\r\n"
//        		+ "expire_cycle_cpu_milliseconds:237\r\n"
//        		+ "evicted_keys:0\r\n"
//        		+ "keyspace_hits:8\r\n"
//        		+ "keyspace_misses:0\r\n"
//        		+ "pubsub_channels:0\r\n"
//        		+ "pubsub_patterns:0\r\n"
//        		+ "latest_fork_usec:17298\r\n"
//        		+ "total_forks:1\r\n"
//        		+ "migrate_cached_sockets:0\r\n"
//        		+ "slave_expires_tracked_keys:0\r\n"
//        		+ "active_defrag_hits:0\r\n"
//        		+ "active_defrag_misses:0\r\n"
//        		+ "active_defrag_key_hits:0\r\n"
//        		+ "active_defrag_key_misses:0\r\n"
//        		+ "tracking_total_keys:0\r\n"
//        		+ "tracking_total_items:0\r\n"
//        		+ "tracking_total_prefixes:0\r\n"
//        		+ "unexpected_error_replies:0\r\n"
//        		+ "total_error_replies:8\r\n"
//        		+ "dump_payload_sanitizations:0\r\n"
//        		+ "total_reads_processed:35511\r\n"
//        		+ "total_writes_processed:16721\r\n"
//        		+ "io_threaded_reads_processed:0\r\n"
//        		+ "io_threaded_writes_processed:0\r\n"
//        		+ "\r\n"
//        		+ "# Replication\r\n"
//        		+ "role:master\r\n"
//        		+ "connected_slaves:1\r\n"
//        		+ "slave0:ip=192.168.56.105,port=17005,state=online,offset=28028,lag=0\r\n"
//        		+ "master_failover_state:no-failover\r\n"
//        		+ "master_replid:e8d3aa2441fbf53d2766e01ffe32262b15efcc3e\r\n"
//        		+ "master_replid2:0000000000000000000000000000000000000000\r\n"
//        		+ "master_repl_offset:28028\r\n"
//        		+ "second_repl_offset:-1\r\n"
//        		+ "repl_backlog_active:1\r\n"
//        		+ "repl_backlog_size:1048576\r\n"
//        		+ "repl_backlog_first_byte_offset:1\r\n"
//        		+ "repl_backlog_histlen:28028\r\n"
//        		+ "\r\n"
//        		+ "# CPU\r\n"
//        		+ "used_cpu_sys:12.553367\r\n"
//        		+ "used_cpu_user:17.318401\r\n"
//        		+ "used_cpu_sys_children:0.018534\r\n"
//        		+ "used_cpu_user_children:0.000000\r\n"
//        		+ "used_cpu_sys_main_thread:11.817680\r\n"
//        		+ "used_cpu_user_main_thread:17.130294\r\n"
//        		+ "\r\n"
//        		+ "# Modules\r\n"
//        		+ "\r\n"
//        		+ "# Errorstats\r\n"
//        		+ "errorstat_ERR:count=8\r\n"
//        		+ "\r\n"
//        		+ "# Cluster\r\n"
//        		+ "cluster_enabled:1\r\n"
//        		+ "\r\n"
//        		+ "# Keyspace\r\n"
//        		+ "db0:keys=1,expires=0,avg_ttl=0\r\n"
//        		+ "";
//        RedisServerStatus redisServerStatus = RedisServerStatus.fromString(input);
//        System.out.println(redisServerStatus);
//    }
}