package org.example.thread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public enum ThreadStates {
    NEW("When thread created but not yet started", ThreadStates::newState),
    RUNNABLE("Thread started", ThreadStates::runnableState),
    WAITING("Thread waiting on lock Object.wait()", ThreadStates::waitingState),
    TIMED_WAITING("Thread waiting on lock with timer Object.wait(500)", ThreadStates::timeWaitingState),
    BLOCKED("Thread blocked on monitor lock", ThreadStates::blockedState),
    TERMINATED("Thread finished execution of run() method", ThreadStates::terminatingState);

    public final String description;
    public final Supplier<String> status;

    ThreadStates(String description, Supplier<String> statusSupplier) {
        this.description = description;
        this.status = statusSupplier;
    }

    private static String newState() {
        var thread = new Thread(() -> {
        });
        return thread.getState().name();
    }

    private static String runnableState() {
        var thread = new Thread(() -> {
        });
        try {
            thread.start();
            return thread.getState().name();
        } finally {
            thread.interrupt();
        }
    }

    private static String waitingState() {
        var thread = new Thread(() -> {
            synchronized (ThreadStates.class) {
                try {
                    ThreadStates.class.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        try {
            thread.start();
            sleep(500);
            return thread.getState().name();
        } finally {
            thread.interrupt();
        }
    }

    private static String timeWaitingState() {
        var s = new Semaphore(0);
        var thread = new Thread(() -> {
            acquireLock(s, 5);
        });
        try {
            thread.start();
            sleep(500);
            return thread.getState().name();
        } finally {
            s.release();
            thread.interrupt();
        }
    }

    private static String blockedState() {
        var one = new Thread(ThreadStates::doWork);
        var two = new Thread(ThreadStates::doWork);
        try {
            one.start();
            two.start();
            sleep(500);
            return two.getState().name();
        } finally {
            one.interrupt();
            two.interrupt();
        }
    }

    private static String terminatingState() {
        var thread = new Thread(() -> {
        });
        thread.start();
        sleep(500);
        return thread.getState().name();
    }

    private static synchronized void doWork() {
        while (true) {

        }
    }

    private static void acquireLock(Semaphore s) {
        try {
            s.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void acquireLock(Semaphore s, long time) {
        try {
            s.tryAcquire(time, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
