package org.xi.maple.common.util;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * @author xishihao
 */
public class RetryUtils {
    private static final Logger logger = LoggerFactory.getLogger(RetryUtils.class);

    public static <R> R retry(Supplier<R> supplier,
                              int times,
                              int retryDuration,
                              R finalDefaultResult,
                              String errMsg) throws InterruptedException {
        if (times <= 0) {
            return finalDefaultResult;
        }

        try {
            return supplier.get();
        } catch (Exception e) {
            logger.error(errMsg);
            Thread.sleep(retryDuration);
            return retry(supplier, times - 1, retryDuration, finalDefaultResult, errMsg);
        }
    }

    public static <R> R retry(Supplier<R> supplier,
                              int times,
                              int retryDuration,
                              String errMsg) throws InterruptedException {
        if (times <= 0) {
            return supplier.get();
        }

        try {
            return supplier.get();
        } catch (Exception e) {
            logger.error(errMsg);
            Thread.sleep(retryDuration);
            return retry(supplier, times - 1, retryDuration, errMsg);
        }
    }

    public static <R> R retry(Supplier<ConditionResult<R>> supplier,
                              int times,
                              R finalDefaultResult,
                              int retryDuration) throws InterruptedException {
        if (times <= 0) {
            return finalDefaultResult;
        }

        ConditionResult<R> result = supplier.get();
        if (result.isSuccess()) {
            return result.getResult();
        }
        Thread.sleep(retryDuration);
        return retry(supplier, times - 1, finalDefaultResult, retryDuration);
    }

    public static <R> R retry(Supplier<ConditionResult<R>> supplier,
                              int times,
                              int retryDuration) throws InterruptedException {
        if (times <= 1) {
            return supplier.get().result;
        }

        ConditionResult<R> result = supplier.get();
        if (result.isSuccess()) {
            return result.getResult();
        }
        Thread.sleep(retryDuration);
        return retry(supplier, times - 1, retryDuration);
    }

    @Data
    public static class ConditionResult<T> {
        private boolean success;
        private T result;
    }
}
