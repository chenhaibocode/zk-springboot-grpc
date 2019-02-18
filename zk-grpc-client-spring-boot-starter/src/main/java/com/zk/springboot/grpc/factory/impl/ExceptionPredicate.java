package com.zk.springboot.grpc.factory.impl;

import com.google.common.base.Predicate;
import com.zk.springboot.grpc.factory.AttemptContext;

import javax.annotation.Nullable;

public final class ExceptionPredicate<T> implements Predicate<AttemptContext<T>> {
    private Predicate<Throwable> delegate;

    public ExceptionPredicate(Predicate<Throwable> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean apply(@Nullable AttemptContext<T> attemptContext) {
        if (!attemptContext.hasException()) {
            return false;
        }
        return delegate.apply(attemptContext.getExceptionCause());
    }
}