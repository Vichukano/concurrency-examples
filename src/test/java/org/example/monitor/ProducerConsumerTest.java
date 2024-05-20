package org.example.monitor;

import org.junit.jupiter.api.Test;

class ProducerConsumerTest {

    @Test
    void consumerProducerMultithreading() throws InterruptedException {
        var factory = new ProducerConsumer(3, new ProducerConsumer.Buffer(1));
        var producer = new Thread(factory::produce);
        var consumerFirst = new Thread(factory::consume);
        var consumerSecond = new Thread(factory::consume);
        var consumerThree = new Thread(factory::consume);

        consumerFirst.start();
        consumerSecond.start();
        consumerThree.start();
        producer.start();

        Thread.sleep(5000);
    }

}