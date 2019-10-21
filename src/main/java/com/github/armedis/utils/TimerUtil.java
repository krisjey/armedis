
package com.github.armedis.utils;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TimerUtil {
    private static final Random SUDO_RANDOM = new Random();

    public static boolean timeToWait(long duration, TimeUnit timeUnit) {
        BlockingQueue<Object> queue = new LinkedBlockingQueue<>();

        try {
            queue.poll(duration, timeUnit);
        }
        catch (InterruptedException e) {
            return false;
        }

        return true;
    }

    public static boolean randomTimeToWait(int maxDuration, TimeUnit timeUnit) {
        BlockingQueue<Object> queue = new LinkedBlockingQueue<>();

        try {
            queue.poll(SUDO_RANDOM.nextInt(maxDuration) + 1, timeUnit);
        }
        catch (InterruptedException e) {
            return false;
        }

        return true;
    }
}
