package com.zk.springboot.grpc.factory.enums;

import com.google.common.base.Preconditions;
import com.zk.springboot.grpc.factory.WaitTimeStategy;
import com.zk.springboot.grpc.factory.impl.ArithmeticProgressionWaitTimeStrategy;
import com.zk.springboot.grpc.factory.impl.ExponentialWaitTimeStrategy;
import com.zk.springboot.grpc.factory.impl.FixedWaitTimeStrategy;
import com.zk.springboot.grpc.factory.impl.RandomWaitTimeStrategy;

import java.util.concurrent.TimeUnit;

public enum WaitTimeStrategies {
    INSTANCE;

    private static final WaitTimeStategy NEVER_WAIT = new FixedWaitTimeStrategy(0L);

    public static WaitTimeStategy neverWait() {
        return NEVER_WAIT;
    }

    public static WaitTimeStategy fixedWaitTime(long waitTimeMs) {
        return fixedWaitTime(waitTimeMs, TimeUnit.MILLISECONDS);
    }

    public static WaitTimeStategy fixedWaitTime(long waitTime, TimeUnit timeUnit) {
        Preconditions.checkNotNull(timeUnit, "time unit cannot be null");
        return new FixedWaitTimeStrategy(timeUnit.toMillis(waitTime));
    }

    public static WaitTimeStategy randomWaitTime(long maxWaitTimeMs) {
        return randomWaitTime(0, TimeUnit.MILLISECONDS, maxWaitTimeMs, TimeUnit.MILLISECONDS);
    }

    public static WaitTimeStategy randomWaitTime(long maxWaitTime, TimeUnit timeUnit) {
        return randomWaitTime(0, TimeUnit.MILLISECONDS, maxWaitTime, timeUnit);
    }

    public static WaitTimeStategy randomWaitTime(long minWaitTimeMs, long maxWaitTimeMs) {
        return randomWaitTime(minWaitTimeMs, TimeUnit.MILLISECONDS, maxWaitTimeMs, TimeUnit.MILLISECONDS);
    }

    public static WaitTimeStategy randomWaitTime(long minWaitTime,
                                                 TimeUnit minWaitTimeUnit,
                                                 long maxWaitTime,
                                                 TimeUnit maxWaitTimeUnit) {
        Preconditions.checkNotNull(minWaitTimeUnit, "min wait time unit cannot be null");
        Preconditions.checkNotNull(maxWaitTimeUnit, "max wait time unit cannot be null");

        return new RandomWaitTimeStrategy(minWaitTimeUnit.toMillis(minWaitTime), maxWaitTimeUnit.toMillis(maxWaitTime));
    }

    public static WaitTimeStategy fixedIncrementWaitTime(long initialMs, long incrementMs) {
        return fixedIncrementWaitTime(initialMs, TimeUnit.MILLISECONDS, incrementMs, TimeUnit.MILLISECONDS);
    }

    public static WaitTimeStategy fixedIncrementWaitTime(long initial,
                                                         TimeUnit initialTimeUnit,
                                                         long increment,
                                                         TimeUnit incrementTimeUnit) {
        Preconditions.checkNotNull(initialTimeUnit, "initial time unit cannot be null");
        Preconditions.checkNotNull(incrementTimeUnit, "increment time unit cannot be null");
        return new ArithmeticProgressionWaitTimeStrategy(
                initialTimeUnit.toMillis(initial),
                incrementTimeUnit.toMillis(increment));
    }


    public static WaitTimeStategy exponentialWaitTime() {
        return exponentialWaitTime(1, Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    public static WaitTimeStategy exponentialWaitTime(long maxWaitTimeMs) {
        return exponentialWaitTime(1, maxWaitTimeMs, TimeUnit.MILLISECONDS);
    }

    public static WaitTimeStategy exponentialWaitTime(long multiplier, long maxWaitTime, TimeUnit maxTimeUnit) {
        Preconditions.checkNotNull(maxTimeUnit, "max time unit cannot be null");
        return new ExponentialWaitTimeStrategy(multiplier, maxTimeUnit.toMillis(maxWaitTime));
    }
}