package org.example.exchanger;

import org.junit.jupiter.api.Test;

class ProducerConsumerViaExchangerExampleTest {

    @Test
    void exchangerExample() throws InterruptedException {
        var example = new ProducerConsumerViaExchangerExample();
        example.run();
    }

}