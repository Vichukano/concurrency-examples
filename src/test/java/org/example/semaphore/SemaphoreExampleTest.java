package org.example.semaphore;

import org.junit.jupiter.api.Test;

class SemaphoreExampleTest {

    @Test
    void semaphoreExampleTest() throws InterruptedException {
        var example = new SemaphoreExample(3);
        example.run();
    }

}