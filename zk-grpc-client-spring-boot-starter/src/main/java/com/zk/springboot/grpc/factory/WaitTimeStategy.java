package com.zk.springboot.grpc.factory;

public interface WaitTimeStategy {
    public long computeCurrentWaitTime(AttemptContext<?> failedAttemptContext);
}