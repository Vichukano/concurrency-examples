package org.example.lock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerWithCondition {
    private final Buffer buffer;
    private int times;

    public ProducerConsumerWithCondition(Buffer buffer, int times) {
        this.buffer = buffer;
        this.times = times;
    }

    public void produce() {
        try {
            while (times >= 0) {
                var value = times--;
                buffer.addValue(value);
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
                var value = buffer.getValue();
                log("value consumed: " + value);
            }
        } catch (InterruptedException e) {
            log("Error: " + e);
            Thread.currentThread().interrupt();
        }
    }

    public static class Buffer {
        private final int size;
        private final Queue<Integer> store;
        private final Lock lock;
        private final Condition readyToConsume;
        private final Condition hasNewElements;

        public Buffer(int size) {
            this.size = size;
            this.store = new LinkedList<>();
            this.lock = new ReentrantLock();
            this.readyToConsume = lock.newCondition();
            this.hasNewElements = lock.newCondition();
        }

        public void addValue(int value) throws InterruptedException {
            try {
                lock.lock();
                //if использовать нельзя для проверки условия!!!
                //только повторная проверка условия через while для пробужденного потока
                while (store.size() == size) {
                    readyToConsume.await();
                }
                store.add(value);
                hasNewElements.signal();
            } finally {
                lock.unlock();
            }
        }

        public Integer getValue() throws InterruptedException {
            try {
                lock.lock();
                while (store.isEmpty()) {
                    hasNewElements.await();
                }
                var value = store.poll();
                readyToConsume.signal();
                return value;
            } finally {
                lock.unlock();
            }
        }
    }

    private static void log(String s) {
        System.out.println(Thread.currentThread().getName() + " " + ProducerConsumerWithCondition.class.getSimpleName() + " = " + s);
    }
}
