package com.github.armedis.http.service.stats;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.armedis.redis.command.RedisCommandExecutor;
import com.github.armedis.redis.connection.pool.RedisConnectionPool;

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;

@Component
public class RedisStatInfoBucket {

	public ObjectNode getStats() {
//		Buffer circularQueue = new CircularFifoBuffer(size);
		CircularFifoQueue<String> queue = new CircularFifoQueue<>(5);

//		queue.
		// TODO Auto-generated method stub
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
			@Autowired
			private RedisConnectionPool<String, String> redisConnectionPool;

			public void run() {
				try {
					StatefulRedisClusterConnection<String, String> connectionPool = redisConnectionPool
							.getClusterConnection();
					String nodes = connectionPool.sync().clusterNodes();
					System.out.println(nodes);

					List<RedisNodeInfo> redisNodeInfo = parseNodeInfo(nodes);
					
					while (true) {
						
						// send info command to each node.
						// calculate tps and etc.
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Hello world Thread.");
			}

			private List<RedisNodeInfo> parseNodeInfo(String nodes) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
