package com.github.armedis.http.service.stats;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.redis.connection.pool.RedisConnectionPool;
import com.github.armedis.redis.info.RedisInfoVo;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;

/**
 * Redis cluster node status info command result --> redis status
 */
@Component
@Configuration
@EnableScheduling
public class RedisStatInfoBucket {
	CircularFifoQueue<String> queue = new CircularFifoQueue<>(20);

	List<RedisClusterNodeInfo> redisNodeInfoList;

	@Autowired
	private RedisConnectionPool<String, String> redisConnectionPool;

	public ObjectNode getStats() {
		// response data 시간 역순으로
		queue.poll();

		return null;
	}

	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(2); // 원하는 크기로 조절
		return scheduler;
	}

	// 초 단위로 메서드를 호출하려면 fixedRate 속성을 사용합니다.
	@Scheduled(fixedRate = 1000) // 1000밀리초 = 1초
	public void myScheduledMethod() {

		// 현재 시간을 가져옵니다.
		LocalDateTime currentTime = LocalDateTime.now();

		// 사용자가 원하는 포맷으로 시간을 문자열로 변환합니다.
		String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		String clusterNodes = getClusterNodesCommandResult(redisConnectionPool);
		redisNodeInfoList = convertNodeInfoList(clusterNodes);

		for (RedisClusterNodeInfo redisNodeInfo : redisNodeInfoList) {
			try {
				StatefulRedisClusterConnection<String, String> connection = redisConnectionPool.getClusterConnection();
				StatefulRedisConnection<String, String> nodeConnection = connection.getConnection(redisNodeInfo.id());

				// send info command
				String info = nodeConnection.sync().info();
				redisConnectionPool.returnObject(connection);

				// update stat info
				RedisInfoVo redisInfo = RedisInfoVo.fromInfoCommandResult(info);

				redisInfo.getServer().setHost(redisNodeInfo.ip());
				redisInfo.getServer().setTcpPort(redisNodeInfo.listenPort());

				System.out.println(formattedTime + " " + redisInfo.getServer().getHost() + ":"
						+ redisInfo.getServer().getTcpPort() + " " + redisNodeInfo.id() + " - " + redisInfo.toJsonString());

				// 현재 시간기준(초단위)
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
