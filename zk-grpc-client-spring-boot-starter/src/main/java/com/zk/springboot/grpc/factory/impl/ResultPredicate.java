package com.zk.springboot.grpc.factory.impl;

import com.google.common.base.Predicate;
import com.zk.springboot.grpc.factory.AttemptContext;

import javax.annotation.Nullable;

public final class ResultPredicate<T> implements Predicate<AttemptContext<T>> {

    private Predicate<T> delegate;

    public ResultPredicate(Predicate<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean apply(@Nullable AttemptContext<T> attemptContext) {
        if (!attemptContext.hasResult()) {
            return false;
        }

        return delegate.apply(attemptContext.getResult());
    }
}