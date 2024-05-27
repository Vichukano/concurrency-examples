package org.example.thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.example.thread.ThreadStates.*;

class ThreadStateTest {

    @Test
    void showThreadStates() {
        Assertions.assertEquals(NEW.status.get(), "NEW");
        Assertions.assertEquals(RUNNABLE.status.get(), "RUNNABLE");
        Assertions.assertEquals(WAITING.status.get(), "WAITING");
        Assertions.assertEquals(TIMED_WAITING.status.get(), "TIMED_WAITING");
        Assertions.assertEquals(BLOCKED.status.get(), "BLOCKED");
        Assertions.assertEquals(TERMINATED.status.get(), "TERMINATED");
    }

}