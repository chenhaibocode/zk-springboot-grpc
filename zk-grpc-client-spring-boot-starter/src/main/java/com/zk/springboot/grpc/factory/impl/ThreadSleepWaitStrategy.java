package com.zk.springboot.grpc.factory.impl;

import com.zk.springboot.grpc.factory.WaitStrategy;

import javax.annotation.concurrent.Immutable;


@Immutable
public final class ThreadSleepWaitStrategy implements WaitStrategy {

    @Override
    public void startWait(long waitTime) throws InterruptedException {
        Thread.sleep(waitTime);
    }
}