package me.doyoung.springcorehigh.trace.callback;

public interface TraceCallback<T> {
    T call();
}
