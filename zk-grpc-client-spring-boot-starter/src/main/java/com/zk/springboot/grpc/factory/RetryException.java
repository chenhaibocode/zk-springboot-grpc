package com.zk.springboot.grpc.factory;

public class RetryException extends Exception {
    private final int numberOfFailedAttempts;
    private final AttemptContext<?> lastFailedAttemptContext;

    public RetryException(int numberOfFailedAttempts, AttemptContext<?> lastFailedAttemptContext) {
        this("Retry failed. Number from attempts: " + numberOfFailedAttempts, numberOfFailedAttempts,
                lastFailedAttemptContext);
    }

    public RetryException(String message, int numberOfFailedAttempts, AttemptContext<?> lastFailedAttemptContext) {
        super(message, lastFailedAttemptContext == null ? null : lastFailedAttemptContext.getExceptionCause());
        this.numberOfFailedAttempts = numberOfFailedAttempts;
        this.lastFailedAttemptContext = lastFailedAttemptContext;
    }

    public int getNumberOfFailedAttempts() {
        return numberOfFailedAttempts;
    }

    public AttemptContext<?> getLastFailedAttemptContext() {
        return lastFailedAttemptContext;
    }
}