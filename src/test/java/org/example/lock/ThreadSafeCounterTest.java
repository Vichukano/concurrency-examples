package org.example.lock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ThreadSafeCounterTest {

    @Test
    void threadSafeCounterTest() throws InterruptedException {
        var tsCounter = new ThreadSafeCounter(0);
        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                tsCounter.increment();
            }
        };
        var threads = List.of(
                new Thread(task),
                new Thread(task),
                new Thread(task),
                new Thread(task),
                new Thread(task),
                new Thread(task),
                new Thread(task),
                new Thread(task),
                new Thread(task),
                new Thread(task)
        );
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }

        Assertions.assertEquals(10000, tsCounter.getCounter());
    }

}