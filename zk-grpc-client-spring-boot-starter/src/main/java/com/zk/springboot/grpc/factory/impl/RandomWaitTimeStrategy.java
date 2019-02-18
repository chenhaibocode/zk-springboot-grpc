package com.zk.springboot.grpc.factory.impl;

import com.google.common.base.Preconditions;
import com.zk.springboot.grpc.factory.AttemptContext;
import com.zk.springboot.grpc.factory.WaitTimeStategy;

import javax.annotation.concurrent.Immutable;
import java.security.SecureRandom;

@Immutable
public final class RandomWaitTimeStrategy implements WaitTimeStategy {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final long minMs;
    private final long delta;

    public RandomWaitTimeStrategy(long minMs, long maxMs) {
        Preconditions.checkArgument(minMs >= 0, "wait time must >= 0");
        Preconditions.checkArgument(maxMs > minMs, "max wait time must > min wait time");

        this.minMs = minMs;
        this.delta = (maxMs - minMs);
    }

    @Override
    public long computeCurrentWaitTime(AttemptContext<?> failedAttemptContext) {
        long nl = RANDOM.nextLong();
        long t = Math.abs(nl);
        if (t == Long.MIN_VALUE) {
            t = 0;
        }
        t = t % delta;

        return t + minMs;
    }
}
