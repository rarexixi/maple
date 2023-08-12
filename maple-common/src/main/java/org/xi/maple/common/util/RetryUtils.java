package org.xi.maple.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * @author xishihao
 */
public class RetryUtils {

    private static final Logger logger = LoggerFactory.getLogger(RetryUtils.class);

    /**
     * 重试
     *
     * @param supplier           重试方法
     * @param times              重试次数
     * @param retryDuration      重试间隔(ms)
     * @param finalDefaultResult 重试失败后的默认返回值
     * @param errMsg             重试失败后的错误信息
     * @param <R>                返回值类型
     * @return 返回值
     */
    public static <R> R retry(Supplier<R> supplier, int times, int retryDuration, R finalDefaultResult, String errMsg) {
        if (times <= 0) {
            return finalDefaultResult;
        }

        try {
            return supplier.get();
        } catch (Exception e) {
            logger.error(errMsg);
            ActionUtils.executeQuietly(() -> Thread.sleep(retryDuration));
            return retry(supplier, times - 1, retryDuration, finalDefaultResult, errMsg);
        }
    }

    /**
     * 重试
     *
     * @param supplier      重试方法
     * @param times         重试次数
     * @param retryDuration 重试间隔(ms)
     * @param errMsg        重试失败后的错误信息
     * @param <R>           返回值类型
     * @return 返回值
     */
    public static <R> R retry(Supplier<R> supplier, int times, int retryDuration, String errMsg) {
        if (times <= 0) {
            return supplier.get();
        }

        try {
            return supplier.get();
        } catch (Exception e) {
            logger.error(errMsg);
            ActionUtils.executeQuietly(() -> Thread.sleep(retryDuration));
            return retry(supplier, times - 1, retryDuration, errMsg);
        }
    }

    /**
     * 重试
     *
     * @param supplier           重试方法
     * @param times              重试次数
     * @param finalDefaultResult 重试失败后的默认返回值
     * @param retryDuration      重试间隔(ms)
     * @param <R>                返回值类型
     * @return 返回值
     */
    public static <R> R retry(Supplier<ConditionResult<R>> supplier, int times, R finalDefaultResult, int retryDuration) {
        if (times <= 0) {
            return finalDefaultResult;
        }

        ConditionResult<R> result = supplier.get();
        if (result.isSuccess()) {
            return result.getResult();
        }
        ActionUtils.executeQuietly(() -> Thread.sleep(retryDuration));
        return retry(supplier, times - 1, finalDefaultResult, retryDuration);
    }

    /**
     * 重试
     *
     * @param supplier      重试方法
     * @param times         重试次数
     * @param retryDuration 重试间隔(ms)
     * @param <R>           返回值类型
     * @return 返回值
     */
    public static <R> R retry(Supplier<ConditionResult<R>> supplier, int times, int retryDuration) {
        if (times <= 1) {
            return supplier.get().result;
        }

        ConditionResult<R> result = supplier.get();
        if (result.isSuccess()) {
            return result.getResult();
        }
        ActionUtils.executeQuietly(() -> Thread.sleep(retryDuration));
        return retry(supplier, times - 1, retryDuration);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConditionResult<T> {
        private boolean success;
        private T result;
    }
}
