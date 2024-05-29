package org.example.latch;

import org.example.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import static org.example.Logger.log;

public class CountDownLatchExample implements Example {
    private final CountDownLatch latch;
    private final int loadersCount;

    public CountDownLatchExample(int loaders) {
        this.latch = new CountDownLatch(loaders);
        this.loadersCount = loaders;
    }

    @Override
    public void run() throws InterruptedException {
        List<ResourceLoader> loaders = new ArrayList<>();
        for (int i = 1; i <= loadersCount; i++) {
            loaders.add(new ResourceLoader(i, latch));
        }
        var handler = new ResourceHandler(loadersCount, latch);
        handler.start();
        for (var loader : loaders) {
            loader.start();
            loader.join();
        }
        handler.join();
    }

    private static class ResourceLoader extends Thread {
        private final int number;
        private final CountDownLatch latch;

        ResourceLoader(int number, CountDownLatch latch) {
            this.number = number;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                log("loader #" + number + " start to load resource...");
                Thread.sleep(ThreadLocalRandom.current().nextInt(300, 800));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        }
    }

    private static class ResourceHandler extends Thread {
        private final int awaitingLoaders;
        private final CountDownLatch latch;

        ResourceHandler(int awaitingLoaders, CountDownLatch latch) {
            this.awaitingLoaders = awaitingLoaders;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                log("handler awaiting for " + awaitingLoaders + " loaders");
                latch.await();
                log("handler finished");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
