package com.zk.springboot.grpc.factory.impl;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.zk.springboot.grpc.factory.AttemptOperation;

import javax.annotation.concurrent.Immutable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Immutable
public final class FixedTimeLimitAttemptOperation<T> implements AttemptOperation<T> {
    private final TimeLimiter timeLimiter;
    private final long duration;
    private final TimeUnit timeUnit;

    public FixedTimeLimitAttemptOperation(long duration) {
        this(null, duration, TimeUnit.MILLISECONDS);
    }

    public FixedTimeLimitAttemptOperation(long duration, TimeUnit timeUnit) {
        this(null, duration, timeUnit);
    }

    public FixedTimeLimitAttemptOperation(long duration, TimeUnit timeUnit, ExecutorService executorService) {
        this(SimpleTimeLimiter.create(executorService), duration, timeUnit);
    }

    public FixedTimeLimitAttemptOperation(TimeLimiter timeLimiter, long duration, TimeUnit timeUnit) {
        if (timeLimiter == null) {
            timeLimiter = SimpleTimeLimiter.create(Executors.newSingleThreadExecutor());
        }

        if (timeUnit == null) {
            timeUnit = TimeUnit.MILLISECONDS;
        }

        this.timeLimiter = timeLimiter;
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    @Override
    public T call(Callable<T> callable) throws Exception {
        return timeLimiter.callWithTimeout(callable, duration, timeUnit);
    }
}
