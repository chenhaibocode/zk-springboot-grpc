package com.zk.springboot.grpc.factory;

import java.util.concurrent.ExecutionException;

public interface AttemptContext<T> {
    public T get() throws ExecutionException;

    public T getResult();

    public ExecutionException getException();

    public boolean hasResult();

    public boolean hasException();

    public Throwable getExceptionCause();

    public int getAttemptCount();

    public long getElapsedTimeSinceFirstAttempt();
}