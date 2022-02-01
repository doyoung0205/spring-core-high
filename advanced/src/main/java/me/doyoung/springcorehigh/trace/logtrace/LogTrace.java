package me.doyoung.springcorehigh.trace.logtrace;

import me.doyoung.springcorehigh.trace.TraceStatus;

public interface LogTrace {

    TraceStatus begin(String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);
}
