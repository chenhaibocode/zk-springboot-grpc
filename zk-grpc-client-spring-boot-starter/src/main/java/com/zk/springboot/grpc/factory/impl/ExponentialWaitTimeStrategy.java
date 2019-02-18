package com.zk.springboot.grpc.factory.impl;

import com.google.common.base.Preconditions;
import com.zk.springboot.grpc.factory.AttemptContext;
import com.zk.springboot.grpc.factory.WaitTimeStategy;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ExponentialWaitTimeStrategy implements WaitTimeStategy {
    private final long multiplier;
    private final long maxWait;

    public ExponentialWaitTimeStrategy(long multiplier, long maxWait) {
        Preconditions.checkArgument(multiplier > 0, "multiplier must > 0");
        Preconditions.checkArgument(maxWait >= 0, "max wait must >= 0");
        Preconditions.checkArgument(multiplier < maxWait, "multiplier must < max wait");

        this.multiplier = multiplier;
        this.maxWait = maxWait;
    }

    @Override
    public long computeCurrentWaitTime(AttemptContext<?> failedAttemptContext) {
        long exp = 1 << (failedAttemptContext.getAttemptCount() - 1);
        long result = multiplier << exp;

        return result <= maxWait ? result : maxWait;
    }
}