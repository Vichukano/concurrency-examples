package org.example.lock;

import org.example.Buffer;
import org.example.Example;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.Logger.log;

public class ProducerConsumerExample implements Example {
    private final int producers;
    private final int consumers;
    private final Buffer<Integer> buffer;
    private final Lock lock;
    private final Condition fullBuffer;
    private final Condition emptyBuffer;

    public ProducerConsumerExample(int bufferSize, int producers, int consumers) {
        this.buffer = new Buffer<>(bufferSize);
        this.producers = producers;
        this.consumers = consumers;
        this.lock = new ReentrantLock();
        this.fullBuffer = lock.newCondition();
        this.emptyBuffer = lock.newCondition();
    }

    @Override
    public void run() throws InterruptedException {
        List<Consumer> consumers = Stream.generate(() -> new Consumer(lock, fullBuffer, buffer, emptyBuffer))
                .limit(this.consumers)
                .toList();
        List<Producer> producers = Stream.generate(() -> new Producer(buffer, lock, fullBuffer, emptyBuffer))
                .limit(this.producers)
                .toList();
        Stream.concat(consumers.stream(), producers.stream())
                .collect(Collectors.toSet())
                .forEach(Thread::start);
        for (Consumer c : consumers) {
            c.join();
        }
        for (Producer p : producers) {
            p.join();
        }
    }

    private static class Producer extends Thread {
        private final Buffer<Integer> buffer;
        private final Lock lock;
        private final Condition fullBuffer;
        private final Condition emptyBuffer;

        private Producer(Buffer<Integer> buffer, Lock lock, Condition fullBuffer, Condition emptyBuffer) {
            this.buffer = buffer;
            this.lock = lock;
            this.emptyBuffer = emptyBuffer;
            this.fullBuffer = fullBuffer;
        }

        @Override
        public void run() {
            try {
                lock.lock();
                while (buffer.isFull()) {
                    fullBuffer.await();
                }
                var value = ThreadLocalRandom.current().nextInt(1, 100);
                buffer.enqueue(value);
                log("put value to buffer: " + value);
                Thread.sleep(ThreadLocalRandom.current().nextInt(300, 900));
                emptyBuffer.signal();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class Consumer extends Thread {
        private final Lock lock;
        private final Condition fullBuffer;
        private final Condition emptyBuffer;
        private final Buffer<Integer> buffer;

        private Consumer(Lock lock,
                         Condition fullBuffer,
                         Buffer<Integer> buffer,
                         Condition emptyBuffer) {
            this.lock = lock;
            this.fullBuffer = fullBuffer;
            this.buffer = buffer;
            this.emptyBuffer = emptyBuffer;
        }

        @Override
        public void run() {
            try {
                lock.lock();
                while (buffer.isEmpty()) {
                    emptyBuffer.await();
                }
                Integer value = buffer.dequeue();
                log("poll valued from buffer: " + value);
                Thread.sleep(ThreadLocalRandom.current().nextInt(300, 900));
                fullBuffer.signal();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }
}
