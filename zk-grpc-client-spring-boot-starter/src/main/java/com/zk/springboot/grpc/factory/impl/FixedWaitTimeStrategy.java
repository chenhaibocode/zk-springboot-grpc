package com.zk.springboot.grpc.factory.impl;

import com.google.common.base.Preconditions;
import com.zk.springboot.grpc.factory.AttemptContext;
import com.zk.springboot.grpc.factory.WaitTimeStategy;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class FixedWaitTimeStrategy implements WaitTimeStategy {
    private final long waitTime;

    public FixedWaitTimeStrategy(long waitTime) {
        Preconditions.checkArgument(waitTime >= 0, "wait time must >= 0");
        this.waitTime = waitTime;
    }

    @Override
    public long computeCurrentWaitTime(AttemptContext<?> failedAttemptContext) {
        return waitTime;
    }
}