package org.example;

import java.time.Instant;

public class Logger {

    public static void log(String s) {
        System.out.println(
                Thread.currentThread().getName()
                        + " | "
                        + Instant.now()
                        + " | "
                        + s
        );
    }

    public static void log(String s, Class<?> clazz) {
        System.out.println(
                clazz.getSimpleName()
                        + " | "
                        + Thread.currentThread().getName()
                        + " | "
                        + Instant.now()
                        + " | "
                        + s
        );
    }

}
