package com.github.armedis.http.service.stats;

import java.io.IOException;
import java.io.StringReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.armedis.redis.connection.pool.RedisConnectionPool;
import com.github.armedis.redis.info.RedisInfoVo;
import com.github.armedis.redis.info.Server;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;

/**
 * Redis cluster node status info command result --> redis status
 */
@Component
@Configuration
@EnableScheduling
public class RedisStatInfoBucket {
	private CircularFifoQueue<RedisStatsInfo> redisStatsInfoList = new CircularFifoQueue<>(20);

	private List<RedisClusterNodeInfo> redisNodeInfoList;

	private Map<String, RedisInfoVo> lastStats;

	private ObjectMapper mapper = configMapper();

	@Autowired
	private RedisConnectionPool<String, String> redisConnectionPool;

	public String getStats() {
		String stats = null;
		try {
			stats = mapper.writeValueAsString(redisStatsInfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stats;
	}

	/**
	 * @return
	 */
	private ObjectMapper configMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setSerializationInclusion(Include.ALWAYS);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		return mapper;
	}

	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(2); // 원하는 크기로 조절
		return scheduler;
	}

	// 초 단위로 메서드를 호출하려면 fixedRate 속성을 사용합니다.
	@Scheduled(fixedRate = 1000) // 1000밀리초 = 1초
	public void myScheduledMethod() throws Throwable {
		ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.systemDefault());

		String clusterNodes = getClusterNodesCommandResult(redisConnectionPool);
		redisNodeInfoList = convertNodeInfoList(clusterNodes);

		RedisStatsInfo redisStatsInfo = new RedisStatsInfo(currentTime);

		String info = null;

		// statsInfo
		for (RedisClusterNodeInfo redisNodeInfo : redisNodeInfoList) {
			try {
				StatefulRedisClusterConnection<String, String> connection = redisConnectionPool.getClusterConnection();
				StatefulRedisConnection<String, String> nodeConnection = connection.getConnection(redisNodeInfo.id());

				// send info command
				info = nodeConnection.sync().info();
				redisConnectionPool.returnObject(connection);

				// update stat info
				RedisInfoVo redisInfo = RedisInfoVo.fromInfoCommandResult(info);

				redisInfo.getServer().setHost(redisNodeInfo.ip());
				redisInfo.getServer().setTcpPort(redisNodeInfo.listenPort());

//				System.out.println(redisStatsInfo.getFormatedEpochTime() + " " + redisInfo.getServer().getHost() + ":"
//						+ redisInfo.getServer().getTcpPort() + " " + redisNodeInfo.id() + " - "
//						+ redisInfo.toJsonString());

				String redisInfoId = redisInfo.getServer().getHost() + ":" + redisInfo.getServer().getTcpPort();
				redisStatsInfo.put(redisInfoId, redisInfo);
				// 현재 시간기준(초단위)
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// calculate sum.
		// Create dummy info from last data.
		RedisInfoVo sumRedisInfo = RedisInfoVo.fromInfoCommandResult(info);

		for (Entry<String, RedisInfoVo> item : redisStatsInfo.getRedisInfoList().entrySet()) {
			RedisInfoVo redisInfoVo = item.getValue();
			// server sum
			Server server = redisInfoVo.getServer();
			// 값이 같으면 이전값 다르면 뒤에 별 붙이기, 숫자는 skip?
			// clients sum
			// memory sum
			// persistance sum
			// stats sum
			// replication sum
			// cpu sum
			// modules sum
			// error stats sum
			// cluster sum
			// keyspace sum.
			// delta count.. --> prev가 없으면 그냥 0 돌려주기.
		}

		redisStatsInfo.put("sum", sumRedisInfo);

		if (redisStatsInfoList.isAtFullCapacity()) {
			redisStatsInfoList.remove();
		}
		redisStatsInfoList.add(redisStatsInfo);

		lastStats = redisStatsInfo.getRedisInfoList();
	}

	private String getClusterNodesCommandResult(RedisConnectionPool<String, String> redisConnectionPool) {
		String nodes = null;
		try {
			StatefulRedisClusterConnection<String, String> connection = redisConnectionPool.getClusterConnection();
			nodes = connection.sync().clusterNodes();
			redisConnectionPool.returnObject(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nodes;
	}

	private List<RedisClusterNodeInfo> convertNodeInfoList(String clusterNodes) {
		List<RedisClusterNodeInfo> redisNodeInfo = new ArrayList<RedisClusterNodeInfo>();
		try {
			List<String> nodeInfoStrings = IOUtils.readLines(new StringReader(clusterNodes));

			for (String nodeInfoString : nodeInfoStrings) {
				RedisClusterNodeInfo nodeInfo = RedisClusterNodeInfoConverter.convert(nodeInfoString);

				redisNodeInfo.add(nodeInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return redisNodeInfo;
	}
}
