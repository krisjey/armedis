package com.github.armedis.http.service.stats;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.redis.connection.pool.RedisConnectionPool;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;

@Component
public class RedisStatInfoBucket {
	CircularFifoQueue<String> queue = new CircularFifoQueue<>(20);

	public ObjectNode getStats() {
		queue.poll();

		return null;
	}

	@Bean(name = "executor")
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	@Bean
	public CommandLineRunner schedulingRunner(TaskExecutor executor, Thread scheduleStatRunner) {
		return args -> {
			executor.execute(scheduleStatRunner);
		};
	}

	@Bean(name = "scheduleStatRunner")
	public Thread scheduleStatRunner() {
		return new Thread() {
			List<RedisNodeInfo> redisNodeInfoList;
			BlockingQueue<String> tempQueue = new LinkedBlockingQueue<>(1);

			@Autowired
			private RedisConnectionPool<String, String> redisConnectionPool;

			public void run() {
				String nodes = getRedisNodeInfo(redisConnectionPool);

				// initialize Redis cluster node info when server startup.

				System.out.println("Start Hello world Thread.");

				while (true) {
					redisNodeInfoList = convertToNodeInfoList(nodes);

					for (RedisNodeInfo redisNodeInfo : redisNodeInfoList) {
						// send info command to each node.
						// calculate tps and etc.

						try {
							StatefulRedisClusterConnection<String, String> connection = redisConnectionPool
									.getClusterConnection();
							StatefulRedisConnection<String, String> nodeConnection = connection
									.getConnection(redisNodeInfo.id());

							String info = nodeConnection.sync().info();
							redisConnectionPool.returnObject(connection);

							System.out.println(info);
							System.out.println(redisNodeInfo.id());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					try {
						tempQueue.poll(1, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			private String getRedisNodeInfo(RedisConnectionPool<String, String> redisConnectionPool) {
				String nodes = null;
				try {
					StatefulRedisClusterConnection<String, String> connection = redisConnectionPool
							.getClusterConnection();
					nodes = connection.sync().clusterNodes();
					redisConnectionPool.returnObject(connection);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return nodes;
			}

			private List<RedisNodeInfo> convertToNodeInfoList(String clusterNodes) {
				List<RedisNodeInfo> redisNodeInfo = new ArrayList<RedisNodeInfo>();
				try {
					List<String> nodeInfoStrings = IOUtils.readLines(new StringReader(clusterNodes));

					for (String nodeInfoString : nodeInfoStrings) {
						// nodeInfoString(12, 10) ==> id, ip, listenPort, clusterBusPort,
						// flags,masterId,
						// pingSend, pongRecv, configEpoch, linkState, shardSlotStart,
						// shardSlotEnd
						String[] nodeInfoArray = StringUtils.split(nodeInfoString, " :@");

						RedisNodeInfo nodeInfo = new RedisNodeInfo();
						nodeInfo.id(nodeInfoArray[0]);
						nodeInfo.ip(nodeInfoArray[1]);
						nodeInfo.listenPort(Integer.parseInt(nodeInfoArray[2]));
						nodeInfo.clusterBusPort(Integer.parseInt(nodeInfoArray[3]));
						nodeInfo.flags(nodeInfoArray[4]);
						nodeInfo.masterId(nodeInfoArray[5]);
						nodeInfo.pingSend(Long.parseLong(nodeInfoArray[6]));
						nodeInfo.pongRecv(Long.parseLong(nodeInfoArray[7]));
						nodeInfo.configEpoch(Integer.parseInt(nodeInfoArray[8]));
						nodeInfo.linkState(nodeInfoArray[9]);
						if (nodeInfoArray.length > 10) {
							nodeInfo.shardSlotStart(Integer.parseInt(StringUtils.split(nodeInfoArray[10], "-")[0]));
							nodeInfo.shardSlotEnd(Integer.parseInt(StringUtils.split(nodeInfoArray[10], "-")[1]));
						}

						redisNodeInfo.add(nodeInfo);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				return redisNodeInfo;
			}
		};
	}
}
