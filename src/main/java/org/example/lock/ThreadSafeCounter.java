package org.example.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeCounter {
    private final Lock lock = new ReentrantLock();
    private int counter;

    public ThreadSafeCounter(int initialValue) {
        this.counter = initialValue;
    }

    public void increment() {
        try {
            lock.lock();
            counter++;
        } finally {
            lock.unlock();
        }
    }

    public int getCounter() {
        return counter;
    }

}
