package com.zk.springboot.grpc.factory.enums;

import com.zk.springboot.grpc.factory.StopStrategy;
import com.zk.springboot.grpc.factory.impl.NeverStopStrategy;
import com.zk.springboot.grpc.factory.impl.StopAfterMaxAttemptStrategy;
import com.zk.springboot.grpc.factory.impl.StopAfterMaxDelayStrategy;

public enum StopStrategies {
    INSTANCE;

    private static final StopStrategy NEVER_STOP = new NeverStopStrategy();

    public static StopStrategy neverStop() {
        return NEVER_STOP;
    }

    public static StopStrategy stopAfterMaxAttempt(int maxAttemptNumber) {
        return new StopAfterMaxAttemptStrategy(maxAttemptNumber);
    }

    public static StopStrategy stopAfterMaxDelay(long maxDelayMs) {
        return new StopAfterMaxDelayStrategy(maxDelayMs);
    }
}