package com.zk.springboot.grpc.factory.impl;

import com.zk.springboot.grpc.factory.AttemptContext;

import javax.annotation.concurrent.Immutable;
import java.util.concurrent.ExecutionException;

@Immutable
public final class ExceptionAttemptContext<T> implements AttemptContext<T> {
    private final ExecutionException e;
    private final int attemptCount;
    private final long elapsedTimeSinceFirstAttempt;

    public ExceptionAttemptContext(Throwable cause, int attemptCount, long elapsedTimeSinceFirstAttempt) {
        this.e = new ExecutionException(cause);
        this.attemptCount = attemptCount;
        this.elapsedTimeSinceFirstAttempt = elapsedTimeSinceFirstAttempt;
    }

    @Override
    public T get() throws ExecutionException {
        throw e;
    }

    @Override
    public T getResult() {
        throw new IllegalStateException("The attempt resulted in an exception. There is no valid result");
    }

    @Override
    public ExecutionException getException() {
        return e;
    }

    @Override
    public boolean hasResult() {
        return false;
    }

    @Override
    public boolean hasException() {
        return true;
    }

    @Override
    public Throwable getExceptionCause() {
        return e.getCause();
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