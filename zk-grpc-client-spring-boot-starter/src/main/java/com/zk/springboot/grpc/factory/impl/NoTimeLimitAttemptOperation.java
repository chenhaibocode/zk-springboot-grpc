package com.zk.springboot.grpc.factory.impl;

import com.zk.springboot.grpc.factory.AttemptOperation;

import javax.annotation.concurrent.Immutable;
import java.util.concurrent.Callable;

@Immutable
public final class NoTimeLimitAttemptOperation<T> implements AttemptOperation<T> {
    @Override
    public T call(Callable<T> callable) throws Exception {
        return callable.call();
    }
}