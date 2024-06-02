package org.example.barier;

import org.example.Example;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.example.Logger.log;

public class CyclicBarrierExample implements Example {
    private final int threads;
    private final CyclicBarrier barrier;

    public CyclicBarrierExample(int threads) {
        this.threads = threads;
        this.barrier = new CyclicBarrier(threads, () -> log("synchronized " + threads + " threads"));
    }

    @Override
    public void run() throws InterruptedException {
        List<Task> tasks = Stream.generate(() -> new Task(barrier))
                .limit(threads)
                .toList();
        tasks.forEach(Thread::start);
        for (var task : tasks) {
            task.join();
        }
    }

    private static class Task extends Thread {
        private final CyclicBarrier barrier;

        private Task(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 2000));
                log("do some work...");
                barrier.await();
                log("work finished!!!");
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
