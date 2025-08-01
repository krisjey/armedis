package com.github.armedis.http.service.stats;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.zookeeper.util.CircularBlockingQueue;
import org.junit.jupiter.api.Test;

class CircularQueueTest {

	@Test
	void testCircularFifoQueue() {
		int queueCapacity = 5;
		CircularFifoQueue<String> queue = new CircularFifoQueue<>(queueCapacity);

		queue.offer("test1");
		queue.offer("test2");
		queue.offer("test3");

		assertThat(queue.maxSize()).isEqualTo(queueCapacity);
		assertThat(queue.isAtFullCapacity()).isEqualTo(false);
		assertThat(queue.isFull()).isEqualTo(false);

		queue.offer("test4");
		queue.offer("test5");
		assertThat(queue.isAtFullCapacity()).isEqualTo(true);
		assertThat(queue.isFull()).isEqualTo(false);
		assertThat(queue.size()).isEqualTo(queueCapacity);

		queue.offer("test6");

		assertThat(queue.element()).isEqualTo("test2");
		assertThat(queue.poll()).isEqualTo("test2");
		assertThat(queue.element()).isEqualTo("test3");

	}

	@Test
	void testCircularBlockingQueue() throws InterruptedException {
		int queueCapacity = 5;
		CircularBlockingQueue<String> queue = new CircularBlockingQueue<>(queueCapacity);

		queue.offer("test1");
		queue.offer("test2");
		queue.offer("test3");

		assertThat(queue.size()).isEqualTo(3);
		assertThat(queue.isEmpty()).isEqualTo(false);

		queue.offer("test4");
		queue.offer("test5");
		assertThat(queue.size()).isEqualTo(queueCapacity);

		queue.offer("test6");

		assertThat(queue.poll(1, TimeUnit.SECONDS)).isEqualTo("test2");
		assertThat(queue.poll(1, TimeUnit.SECONDS)).isEqualTo("test3");
		assertThat(queue.poll(1, TimeUnit.SECONDS)).isEqualTo("test4");
		assertThat(queue.poll(1, TimeUnit.SECONDS)).isEqualTo("test5");
		assertThat(queue.poll(1, TimeUnit.SECONDS)).isEqualTo("test6");
	}

}
