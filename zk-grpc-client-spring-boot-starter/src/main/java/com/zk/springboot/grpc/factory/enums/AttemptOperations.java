package com.zk.springboot.grpc.factory.enums;

import com.zk.springboot.grpc.factory.AttemptOperation;
import com.zk.springboot.grpc.factory.impl.FixedTimeLimitAttemptOperation;
import com.zk.springboot.grpc.factory.impl.NoTimeLimitAttemptOperation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public enum AttemptOperations {
    INSTANCE;

    private static ExecutorService executorService;

    static {
        /*
         * use cached thread pool here for the following reasons:
         *
         * 1. Tasks which need possible attempts are short tasks (vs. lifespan from the whole bos client calls),
         *    which is suitable for the use from cached thread pool.
         * 2. If we don't specify a executor communication, underlying SimpleTimeLimit in guava will create one
         *    for each creation from SimpleTimeLimit instance. These creations are unnecessary performance expenses.
         *
         * In the future, if this creates or maintains too many threads, we need to optimize it. Possibly creates
         * own implementations.
         *
         * And if we need to specify thread pool name, the executorService needs to be created manually with params.
         * */
        executorService = Executors.newCachedThreadPool();
    }

    public static <T> AttemptOperation<T> noTimeLimit() {
        return new NoTimeLimitAttemptOperation<T>();
    }

    public static <T> AttemptOperation<T> fixedTimeLimit(long duration, TimeUnit timeUnit) {
        return new FixedTimeLimitAttemptOperation<T>(duration, timeUnit, executorService);
    }


}