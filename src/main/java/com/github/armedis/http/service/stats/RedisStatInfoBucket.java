package com.github.armedis.http.service.stats;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;

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
			
			public void run() {
				System.out.println("Hello world Thread.");
			}
		};
	}
}
