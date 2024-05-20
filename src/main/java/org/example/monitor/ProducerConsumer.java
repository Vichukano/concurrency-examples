package org.example.monitor;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {
    private int times;
    private final Buffer buffer;


    public ProducerConsumer(int times, Buffer buffer) {
        this.times = times;
        this.buffer = buffer;
    }

    public void produce() {
        try {
            while (times >= 0) {
                var value = times--;
                buffer.add(value);
                log("value produced: " + value);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            log("Error: " + e);
            Thread.currentThread().interrupt();
        }
    }

    public void consume() {
        try {
            while (times >= 0) {
                Thread.sleep(1000);
                var value = buffer.get();
                log("value consumed: " + value);
            }

        } catch (InterruptedException e) {
            log("Error: " + e);
            Thread.currentThread().interrupt();
        }
    }

    public static class Buffer {
        private final Object LOCK = new Object();
        private final int size;
        private final Queue<Integer> queue;

        public Buffer(int size) {
            this.size = size;
            this.queue = new LinkedList<>();
        }

        public void add(int value) throws InterruptedException {
            synchronized (LOCK) {
                while (queue.size() == size) {
                    LOCK.wait();
                }
                queue.add(value);
                LOCK.notifyAll();
            }
        }


        public Integer get() throws InterruptedException {
            synchronized (LOCK) {
                while (queue.isEmpty()) {
                    LOCK.wait();
                }
                LOCK.notifyAll();
                return queue.poll();
            }
        }

    }

    private static void log(String s) {
        System.out.println(Thread.currentThread().getName() + " " + ProducerConsumer.class.getSimpleName() + " = " + s);
    }
}
