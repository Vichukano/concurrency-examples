package org.example.barier;

import org.junit.jupiter.api.Test;

class CyclicBarrierExampleTest {

    @Test
    void shouldSynchronizeThreadsInOnePoint() throws InterruptedException {
        var example = new CyclicBarrierExample(3);
        example.run();
    }

}