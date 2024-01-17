package org.xi.maple.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.common.function.Action;

import java.util.concurrent.ScheduledFuture;

/**
 * @author xishihao
 */
public class ActionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ActionUtils.class);


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

    public static void cancelScheduledFutureQuietly(ScheduledFuture<?> scheduledFuture) {
        cancelScheduledFutureQuietly(scheduledFuture, true);
    }

    public static void cancelScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        cancelScheduledFuture(scheduledFuture, true);
    }

    public static void cancelScheduledFutureQuietly(ScheduledFuture<?> scheduledFuture, boolean mayInterruptIfRunning) {
        executeQuietly(() -> ActionUtils.cancelScheduledFuture(scheduledFuture, mayInterruptIfRunning));
    }

    public static void cancelScheduledFuture(ScheduledFuture<?> scheduledFuture, boolean mayInterruptIfRunning) {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(mayInterruptIfRunning);
        }
    }
}
