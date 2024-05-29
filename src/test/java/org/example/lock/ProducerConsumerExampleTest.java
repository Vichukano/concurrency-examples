package org.example.lock;

import org.example.Example;
import org.junit.jupiter.api.Test;

class ProducerConsumerExampleTest {

    @Test
    void producerConsumerPatternExample() throws InterruptedException {
        Example example = new ProducerConsumerExample(3, 9, 8);
        example.run();
    }

}