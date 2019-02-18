package com.zk.springboot.grpc.factory;

public interface RetryListener {
    public <T> void onRetry(AttemptContext<T> attemptContext);
}