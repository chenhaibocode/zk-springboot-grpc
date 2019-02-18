package com.zk.springboot.grpc.factory;

import java.util.concurrent.Callable;

public interface AttemptOperation<T> {
    T call(Callable<T> callable) throws Exception;
}