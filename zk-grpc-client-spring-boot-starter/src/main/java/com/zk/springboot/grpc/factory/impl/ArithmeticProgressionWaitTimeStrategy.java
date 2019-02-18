package com.zk.springboot.grpc.factory.impl;

import com.google.common.base.Preconditions;
import com.zk.springboot.grpc.factory.AttemptContext;
import com.zk.springboot.grpc.factory.WaitTimeStategy;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ArithmeticProgressionWaitTimeStrategy implements WaitTimeStategy {
    private final long initial;
    private final long commonDifference;        // can be <= 0. but if final result < 0, we return 0

    public ArithmeticProgressionWaitTimeStrategy(long initial, long commonDifference) {
        Preconditions.checkArgument(initial >= 0, "initial wait time must >= 0");

        this.initial = initial;
        this.commonDifference = commonDifference;
    }

    @Override
    public long computeCurrentWaitTime(AttemptContext<?> failedAttemptContext) {
        long result = initial + (commonDifference * (failedAttemptContext.getAttemptCount() - 1));
        return result >= 0L ? result : 0L;
    }
}