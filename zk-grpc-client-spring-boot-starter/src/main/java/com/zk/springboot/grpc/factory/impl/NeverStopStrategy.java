package com.zk.springboot.grpc.factory.impl;

import com.zk.springboot.grpc.factory.AttemptContext;
import com.zk.springboot.grpc.factory.StopStrategy;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class NeverStopStrategy implements StopStrategy {
    @Override
    public boolean shouldStop(AttemptContext<?> failedAttemptContext) {
        return false;
    }
}