package com.zk.springboot.grpc.factory;

public interface StopStrategy {
    boolean shouldStop(AttemptContext<?> failedAttemptContext);
}
