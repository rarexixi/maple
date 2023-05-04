package org.xi.maple.common.util;

/**
 * @author xishihao
 */
public class ActionUtils {

    @FunctionalInterface
    public interface Action {
        void run() throws Throwable;
    }

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
