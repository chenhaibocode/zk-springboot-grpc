package com.zk.springboot.grpc.factory;

public interface WaitStrategy {
    public void startWait(long waitTime) throws InterruptedException;
}