package org.example.latch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CountDownLatchExampleTest {

    @Test
    @DisplayName("handler should wait until all loaders finished work")
    void countDownLatchExample() throws InterruptedException {
        new CountDownLatchExample(10).run();
    }

}