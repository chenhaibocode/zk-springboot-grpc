package com.zk.springboot.grpc.factory.impl;

import com.google.common.base.Preconditions;
import com.zk.springboot.grpc.factory.AttemptContext;
import com.zk.springboot.grpc.factory.StopStrategy;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class StopAfterMaxAttemptStrategy implements StopStrategy {
    private final int maxAttemptNumber;

    public StopAfterMaxAttemptStrategy(int maxAttemptNumber) {
        Preconditions.checkArgument(maxAttemptNumber >= 1, "maxAttemptNumber must >= 1");

        this.maxAttemptNumber = maxAttemptNumber;
    }

    @Override
    public boolean shouldStop(AttemptContext<?> failedAttemptContext) {
        return failedAttemptContext.getAttemptCount() >= maxAttemptNumber;
    }
}