package org.xi.maple.common.function;

@FunctionalInterface
public interface ThrowableConsumer<T> {

    void accept(T t) throws Throwable;
}