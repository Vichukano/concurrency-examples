package org.example.lock;

import org.junit.jupiter.api.Test;

class ProducerConsumerWithConditionTest {

    @Test
    void producerConsumerTest() throws InterruptedException {
        var factory = new ProducerConsumerWithCondition(
                new ProducerConsumerWithCondition.Buffer(1), 10
        );
        var producer = new Thread(factory::produce);
        var consumerFirst = new Thread(factory::consume);
        var consumerSecond = new Thread(factory::consume);
        var consumerThree = new Thread(factory::consume);

        consumerFirst.start();
        consumerSecond.start();
        consumerThree.start();
        producer.start();

        Thread.sleep(9000);
    }

}