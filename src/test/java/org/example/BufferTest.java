package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BufferTest {

    @Test
    void shouldAddAndRemoveValues() {
        var buffer = new Buffer<Integer>(3);
        buffer.enqueue(1);
        buffer.enqueue(2);
        buffer.enqueue(3);

        Assertions.assertEquals(3, buffer.dequeue());
        Assertions.assertEquals(2, buffer.dequeue());
        Assertions.assertEquals(1, buffer.dequeue());
    }

}