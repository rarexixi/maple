package org.xi.maple.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xishihao
 */
public class ActionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ActionUtils.class);

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
        } catch (Throwable t) {
            logger.error("执行失败", t);
        }
    }
}
