package com.zk.springboot.grpc.factory.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.zk.springboot.grpc.factory.AttemptContext;

import javax.annotation.Nullable;

public final class ExceptionClassPredicate<T> implements Predicate<AttemptContext<T>> {
    private Class<? extends Throwable> exceptionClass;

    public ExceptionClassPredicate(Class<? extends Throwable> exceptionClass) {
        Preconditions.checkNotNull(exceptionClass, "exception class cannot be null");
        this.exceptionClass = exceptionClass;
    }

    @Override
    public boolean apply(@Nullable AttemptContext<T> attemptContext) {
        if (!attemptContext.hasException()) {
            return false;
        }
        return exceptionClass.isAssignableFrom(attemptContext.getExceptionCause().getClass());
    }
}