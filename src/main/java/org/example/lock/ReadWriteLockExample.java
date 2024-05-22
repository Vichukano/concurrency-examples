package org.example.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample {
    private final List<String> store;
    private final ReadWriteLock rwLock;
    private final Lock readLock;
    private final Lock writeLock;

    public ReadWriteLockExample() {
        this.store = new ArrayList<>();
        this.rwLock = new ReentrantReadWriteLock();
        this.readLock = rwLock.readLock();
        this.writeLock = rwLock.writeLock();
    }

    /*только одни поток одновременно будет изменять данные*/
    public void add(String s) {
        try {
            writeLock.lock();
            store.add(s);
        } finally {
            writeLock.unlock();
        }
    }

    /*
    пока есть блокировка на запись, то не читает никто
    если блокировки на запись нет, то читают все
    */
    public List<String> getStore() {
        try {
            readLock.lock();
            return new ArrayList<>(store);
        } finally {
            readLock.unlock();
        }
    }

}
