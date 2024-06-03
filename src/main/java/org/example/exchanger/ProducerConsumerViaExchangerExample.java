package org.example.exchanger;

import org.example.Buffer;
import org.example.Example;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadLocalRandom;

import static org.example.Logger.log;

public class ProducerConsumerViaExchangerExample implements Example {

    @Override
    public void run() throws InterruptedException {
        var buffer = new Buffer<Integer>(5);
        var exchanger = new Exchanger<Buffer<Integer>>();
        int limit = 3;
        var producer = new ProducerTask(buffer, exchanger, limit);
        var consumer = new ConsumerTask(buffer, exchanger, limit);
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    private static class ConsumerTask extends Thread {
        private final Exchanger<Buffer<Integer>> exchanger;
        private Buffer<Integer> buffer;
        private int times;

        private ConsumerTask(Buffer<Integer> buffer, Exchanger<Buffer<Integer>> exchanger, int times) {
            this.buffer = buffer;
            this.exchanger = exchanger;
            this.times = times;
        }

        @Override
        public void run() {
            try {
                while (times >= 0) {
                    while (!buffer.isEmpty()) {
                        var value = buffer.dequeue();
                        Thread.sleep(ThreadLocalRandom.current().nextInt(200, 999));
                        log("consumed value: " + value);
                    }
                    this.buffer = exchanger.exchange(buffer);
                    times--;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class ProducerTask extends Thread {
        private final Exchanger<Buffer<Integer>> exchanger;
        private Buffer<Integer> buffer;
        private int times;

        public ProducerTask(Buffer<Integer> buffer, Exchanger<Buffer<Integer>> exchanger, int times) {
            this.buffer = buffer;
            this.exchanger = exchanger;
            this.times = times;
        }

        @Override
        public void run() {
            try {
                while (times >= 0) {
                    while (!buffer.isFull()) {
                        var value = ThreadLocalRandom.current().nextInt(1, 999);
                        Thread.sleep(ThreadLocalRandom.current().nextInt(200, 999));
                        buffer.enqueue(value);
                        log("produced value: " + value);
                    }
                    this.buffer = exchanger.exchange(buffer);
                    times--;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
