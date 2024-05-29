package org.example.semaphore;

import org.example.Example;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.example.Logger.log;

public class SemaphoreExample implements Example {
    private final Semaphore semaphore;

    public SemaphoreExample(int maxAccessCount) {
        this.semaphore = new Semaphore(maxAccessCount);
    }

    @Override
    public void run() throws InterruptedException {
        List<Task> tasks = Stream.generate(() -> new Task(semaphore))
                .limit(10)
                .toList();
        tasks.forEach(Thread::start);
        for (Task t : tasks) {
            t.join();
        }
    }

    private static class Task extends Thread {
        private final Semaphore semaphore;

        Task(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                log("run some task...");
                Thread.sleep(ThreadLocalRandom.current().nextInt(800, 2000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
            }
        }
    }
}
