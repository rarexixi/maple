package org.xi.maple.common.function;

@FunctionalInterface
public interface ThrowableFunction<T, R> {

    R apply(T t) throws Throwable;
}