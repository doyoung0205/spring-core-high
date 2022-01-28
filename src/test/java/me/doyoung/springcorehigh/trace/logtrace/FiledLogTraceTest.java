package me.doyoung.springcorehigh.trace.logtrace;

import me.doyoung.springcorehigh.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class FiledLogTraceTest {
    @Test
    void begin_end() {
        final LogTrace trace = new FiledLogTrace();
        final TraceStatus status1 = trace.begin("hello");
        final TraceStatus status2 = trace.begin("hello2");
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    void begin_exception() {
        final LogTrace trace = new FiledLogTrace();
        final TraceStatus status1 = trace.begin("hello");
        final TraceStatus status2 = trace.begin("hello2");
        trace.exception(status2, new IllegalStateException());
        trace.exception(status1, new IllegalStateException());
    }
}
