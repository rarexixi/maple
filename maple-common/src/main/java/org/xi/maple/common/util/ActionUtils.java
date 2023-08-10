package org.xi.maple.common.util;

/**
 * @author xishihao
 */
public class ActionUtils {

    @FunctionalInterface
    public interface Action {
        void run() throws Throwable;
    }

    /**
     * 执行操作，不抛出异常
     *
     * @param action 要执行的操作
     */
    public static void executeQuietly(Action action) {
        if (action == null) {
            return;
        }
        try {
            action.run();
        } catch (Throwable ignored) {
        }
    }
}
