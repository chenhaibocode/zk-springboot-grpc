package com.zk.springboot.grpc.factory.impl;

import com.zk.springboot.grpc.factory.AttemptContext;

import javax.annotation.concurrent.Immutable;
import java.util.concurrent.ExecutionException;

@Immutable
public final class ResultAttemptContext<T> implements AttemptContext<T> {
    private final T result;
    private final int attemptCount;
    private final long elapsedTimeSinceFirstAttempt;

    public ResultAttemptContext(T result, int attemptCount, long elapsedTimeSinceFirstAttempt) {
        this.result = result;
        this.attemptCount = attemptCount;
        this.elapsedTimeSinceFirstAttempt = elapsedTimeSinceFirstAttempt;
    }

    @Override
    public T get() {
        return result;
    }

    @Override
    public T getResult() {
        return result;
    }

    @Override
    public ExecutionException getException() {
        throw new IllegalStateException("The attempt resulted in a result. There is no exception");
    }

    @Override
    public boolean hasResult() {
        return true;
    }

    @Override
    public boolean hasException() {
        return false;
    }

    @Override
    public Throwable getExceptionCause() {
        throw new IllegalStateException("The attempt resulted in a result. There is no exception");
    }

    @Override
    public int getAttemptCount() {
        return attemptCount;
    }

    @Override
    public long getElapsedTimeSinceFirstAttempt() {
        return elapsedTimeSinceFirstAttempt;
    }
}
