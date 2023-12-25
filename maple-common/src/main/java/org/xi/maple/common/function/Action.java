package org.xi.maple.common.function;

@FunctionalInterface
public interface Action {
    void run() throws Throwable;
}