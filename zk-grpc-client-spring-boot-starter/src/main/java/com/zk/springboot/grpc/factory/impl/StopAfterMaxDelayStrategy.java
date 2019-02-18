package com.zk.springboot.grpc.factory.impl;

import com.google.common.base.Preconditions;
import com.zk.springboot.grpc.factory.AttemptContext;
import com.zk.springboot.grpc.factory.StopStrategy;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class StopAfterMaxDelayStrategy implements StopStrategy {
    private final long maxDelay;

    public StopAfterMaxDelayStrategy(long maxDelay) {
        Preconditions.checkArgument(maxDelay >= 0, "maxDelay must >= 0");
        this.maxDelay = maxDelay;
    }

    @Override
    public boolean shouldStop(AttemptContext<?> failedAttemptContext) {
        return failedAttemptContext.getElapsedTimeSinceFirstAttempt() >= maxDelay;
    }
}