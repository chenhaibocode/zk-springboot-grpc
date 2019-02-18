package com.zk.springboot.grpc.factory;

import com.google.common.base.Predicate;
import com.zk.springboot.grpc.factory.impl.ExceptionAttemptContext;
import com.zk.springboot.grpc.factory.impl.ResultAttemptContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@Slf4j
public class Retryer<T> {
    private final AttemptOperation<T> attemptOperation;
    private final StopStrategy stopStrategy;
    private final WaitStrategy waitStrategy;
    private final WaitTimeStategy waitTimeStategy;
    private final Collection<Predicate<AttemptContext<T>>> resultRejectionPredicates;
    private final Collection<RetryListener> retryListeners;
    private String description;

    public Retryer(AttemptOperation<T> attemptOperation,
                   StopStrategy stopStrategy,
                   WaitStrategy waitStrategy,
                   WaitTimeStategy waitTimeStategy,
                   Collection<Predicate<AttemptContext<T>>> resultRejectionPredicates,
                   Collection<RetryListener> retryListeners) {
        this.attemptOperation = attemptOperation;
        this.stopStrategy = stopStrategy;
        this.waitStrategy = waitStrategy;
        this.waitTimeStategy = waitTimeStategy;
        this.resultRejectionPredicates = resultRejectionPredicates;
        this.retryListeners = retryListeners;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T call(Callable<T> callable) throws ExecutionException, RetryException {
        long startTime = System.currentTimeMillis();

        // 1. here we go
        for (int attemptCount = 1; ; ++attemptCount) {
            AttemptContext<T> attemptContext;

            if (!StringUtils.isEmpty(description)) {
                log.info("attempt count: {}; {}", attemptCount, description);
            }

            // 2. actual call
            try {
                T result = attemptOperation.call(callable);

                // 2.1. successfully called and returned: init context with result
                attemptContext = new ResultAttemptContext<T>(
                        result,
                        attemptCount,
                        System.currentTimeMillis() - startTime);
            } catch (Throwable t) {
                // 2.2. throwable caught in actual call: int context with exception
                attemptContext = new ExceptionAttemptContext<T>(
                        t,
                        attemptCount,
                        System.currentTimeMillis() - startTime);
            }

            // 3. call hooks / listeners registered
            for (RetryListener retryListener : retryListeners) {
                retryListener.onRetry(attemptContext);
            }

            // 4. if it is a result, check if it showld be rejected. if no one rejects, then return it
            boolean isResultOk = true;
            for (Predicate<AttemptContext<T>> resultRejectionPredicate : resultRejectionPredicates) {
                if (resultRejectionPredicate.apply(attemptContext)) {
                    isResultOk = false;
                    break;
                }
            }

            if (isResultOk) {
                return attemptContext.get();
            }

            // 5. check if retry should stop
            if (stopStrategy.shouldStop(attemptContext)) {
                throw new RetryException(attemptCount, attemptContext);
            } else {
                // 6. retry continues. we need to compute current wait time
                long waitTime = waitTimeStategy.computeCurrentWaitTime(attemptContext);

                try {
                    // 7. wait starts
                    waitStrategy.startWait(waitTime);
                } catch (InterruptedException e) {
                    // interrupted, retry exceptions are thrown
                    Thread.currentThread().interrupt();
                    throw new RetryException(attemptCount, attemptContext);
                }
            }
        }


    }
}