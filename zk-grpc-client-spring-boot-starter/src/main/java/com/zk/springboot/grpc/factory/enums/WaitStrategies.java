package com.zk.springboot.grpc.factory.enums;

import com.zk.springboot.grpc.factory.impl.ThreadSleepWaitStrategy;
import com.zk.springboot.grpc.factory.WaitStrategy;

public enum WaitStrategies {
    INSTANCE;

    private static WaitStrategy THREAD_SLEEP_STRATEGY = new ThreadSleepWaitStrategy();

    public static WaitStrategy threadSleepWait() {
        return THREAD_SLEEP_STRATEGY;
    }
}