
/**
 * 
 */

import java.util.Queue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.EvictingQueue;

/**
 * 
 */
class CircularQueueTest {
    private static final int QUEUE_SIZE = 2;

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        // This option increases the response data size.
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.setSerializationInclusion(Include.ALWAYS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    void test() {
        EvictingQueue<Integer> queue = EvictingQueue.create(QUEUE_SIZE);

        // 데이터 1~5 삽입
        for (int i = 1; i <= 5; i++) {
            queue.add(i);
        }

//        // 데이터 출력
//        while (!queue.isEmpty()) {
//            System.out.println(queue.poll());
//        }
        
        String stats = null;
        try {
            stats = mapper.writeValueAsString(queue);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(stats);
    }

    @Test
    void test2() {
        Queue<Integer> queue = EvictingQueue.create(QUEUE_SIZE);

        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        queue.offer(5);

        System.out.println("Queue: " + queue);

        String stats = null;
        try {
            stats = mapper.writeValueAsString(queue);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(stats);
    }

}
