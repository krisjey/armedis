/**
 * 
 */
package com.github.armedis.redis;

import java.util.Random;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.stats.RedisStatInfoBucket;
import com.github.armedis.redis.info.RedisInfoVo;

/**
 * 
 */
@ActiveProfiles("testbed")
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
class RedisInfoVoTest {
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		Random rnd = new Random();
		Integer portNumber = rnd.nextInt(1000) + 8001;
		System.setProperty("SERVICE_PORT", String.valueOf(portNumber));
	}

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	RedisStatInfoBucket bucket;

	@Test
	void test() throws Throwable {
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
				+ "errorstat_ERR:count=8\r\nerrorstat_MOVED:count=1\r\n" + "\r\n" + "# Cluster\r\n" + "cluster_enabled:1\r\n" + "\r\n"
				+ "# Keyspace\r\n" + "db0:keys=1,expires=0,avg_ttl=0\r\n" + "";
		RedisInfoVo redisInfoVO = RedisInfoVo.from(infoResult, false);
//		System.out.println(redisInfoVO.toString());

		ObjectMapper objectMapper = new ObjectMapper();
//		java.lang.IllegalArgumentException: No serializer found for class com.github.armedis.redis.RedisInfoVO$ServerInfo and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.github.armedis.redis.RedisInfoVO["serverInfo"])
//		at com.fasterxml.jackson.databind.ObjectMapper.valueToTree(ObjectMapper.java:3537)
//		at com.github.armedis.redis.RedisInfoVOTest.test(RedisInfoVOTest.java:87)
//		at java.base/java.lang.reflect.Method.invoke(Method.java:568)
//		at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
//		at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
//	Caused by: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class com.github.armedis.redis.RedisInfoVO$ServerInfo and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.github.armedis.redis.RedisInfoVO["serverInfo"])
//		at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
//		at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1308)
//		at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:414)
//		at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
//		at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
//		at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:732)
//		at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:772)
//		at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:178)
//		at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:479)
//		at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:318)
//		at com.fasterxml.jackson.databind.ObjectMapper.valueToTree(ObjectMapper.java:3532)
//		... 4 more

//		JsonNode jsonNode = objectMapper.valueToTree(redisInfoVO);
//		System.out.println(jsonNode);
		String json = objectMapper.writeValueAsString(redisInfoVO);
		System.out.println("Static data convert " + json);

		CircularFifoQueue<RedisInfoVo> queue = new CircularFifoQueue<RedisInfoVo>();
		queue.add(redisInfoVO);
		queue.add(redisInfoVO);

//		redisInfoVO
	}

	@Test
	void test2() {
		System.out.println(bucket.toString());
//		bucket.getStats();
	}
}
