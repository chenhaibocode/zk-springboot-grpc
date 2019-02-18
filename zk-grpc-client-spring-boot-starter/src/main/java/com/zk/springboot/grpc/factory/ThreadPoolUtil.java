package com.zk.springboot.grpc.factory;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

@Slf4j
public class ThreadPoolUtil {

    public static ThreadPoolExecutor getArrayBlockingQueuePoolExecutorRejectDiscard(
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            int blockingQueueSize,
            final String tag,
            RejectedExecutionHandler rejectedExecutionHandler) {

        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new ArrayBlockingQueue<Runnable>(blockingQueueSize),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(tag + "_thread");
                        return thread;
                    }
                },
                rejectedExecutionHandler
        );
    }

    public static class DiscardRejectedExecutionHandler implements RejectedExecutionHandler {

        private String tag;

        public DiscardRejectedExecutionHandler(String tag) {
            this.tag = tag;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.error(
                    tag + "_thread_full: discard QueueSize={}, ActiveCount={}, Core_Max_PoolSize=[{}-{}], CompletedTaskCount={}",
                    executor.getQueue().size(), executor.getActiveCount(), executor.getCorePoolSize(),
                    executor.getMaximumPoolSize(), executor.getCompletedTaskCount());
        }
    }
}