package org.example;

import java.util.Arrays;

public class Buffer<T> {
    private final int maxSize;
    private final Object[] store;
    private int currSize;

    public Buffer(int maxSize) {
        this.maxSize = maxSize;
        this.store = new Object[maxSize];
        this.currSize = 0;
    }

    public void enqueue(T value) {
        if (currSize >= maxSize) {
            throw new IllegalStateException("size overflow");
        }
        store[currSize] = value;
        currSize++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (currSize == 0) {
            throw new IllegalStateException("empty buffer");
        }
        int index = currSize - 1;
        var value = store[index];
        store[index] = null;
        currSize--;
        return (T) value;
    }

    public boolean isEmpty() {
        return currSize == 0;
    }

    public boolean isFull() {
        return currSize == maxSize;
    }

    @Override
    public String toString() {
        return "Buffer["
                + Arrays.toString(store)
                + ']';
    }
}
