package org.xi.maple.redis.util;

import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author xishihao
 */
public class MapleRedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(MapleRedisUtil.class);

    /**
     * 获取作业要添加的队列锁的key
     *
     * @return redis 作业队列锁的 key
     */
    public static String getJobQueueLock(String fromApp, String group, String jobType, String queueName, Integer priority) {
        return String.format("maple-lock-%s%s%s%s%s", fromApp, group, jobType, queueName, priority);
    }

    /**
     * 获取引擎锁
     *
     * @param engineInstanceId 引擎ID
     * @return redis 引擎锁的 key
     */
    public static String getEngineInstanceLock(Integer engineInstanceId) {
        return String.format("maple-lock-engine-%s", engineInstanceId);
    }

    /**
     * 获取作业队列
     *
     * @return redis 队列名
     */
    public static String getJobQueue(String fromApp, String group, String jobType, String queueName, Integer priority) {
        return String.format("maple-group-queue-%s%s%s%s%s", fromApp, group, jobType, queueName, priority);
    }

    /**
     * 获取作业队列
     *
     * @param
     * @return redis 队列名
     */
    public static String getUserJobQueue(String fromApp, String user, String jobType, String queueName, Integer priority) {
        return String.format("maple-group-queue-%s%s%s%s%s", fromApp, user, jobType, queueName, priority);
    }


    /**
     * 尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock     锁
     * @param lockName 锁的名字
     * @param runnable 执行的操作
     */
    public static void tryLockAndExecute(RLock lock, String lockName, Runnable runnable) {
        tryLockAndExecute(lock, lockName, runnable, () -> {
        });
    }

    /**
     * 尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock              锁
     * @param lockName          锁的名字
     * @param runnable          执行的操作
     * @param lockFailOperation 获取锁失败时的操作
     */
    public static void tryLockAndExecute(RLock lock, String lockName, Runnable runnable, Runnable lockFailOperation) {
        if (runnable == null) {
            return;
        }
        boolean hasLocked = false;
        try {
            hasLocked = lock.tryLock();
            if (hasLocked) {
                runnable.run();
            } else {
                if (lockFailOperation != null) {
                    lockFailOperation.run();
                }
            }
        } catch (Exception e) {
            String msg = String.format("Lock [%s] failed", lockName);
            logger.error(msg, e);
        } finally {
            if (hasLocked) {
                try {
                    lock.unlock();
                } catch (Exception e) {
                    // todo 释放锁失败操作
                    String msg = String.format("Unlock [%s] failed", lockName);
                    logger.error(msg, e);
                }
            }
        }
    }

    /**
     * 在尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock     锁
     * @param lockName 锁的名字
     * @param time     等待锁的最大时长
     * @param unit     时间单位
     * @param runnable 执行的操作
     */
    public static void waitLockAndExecute(RLock lock, String lockName, long time, TimeUnit unit, Runnable runnable) {
        waitLockAndExecute(lock, lockName, time, unit, runnable, null);
    }

    /**
     * 尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock              锁
     * @param lockName          锁的名字
     * @param runnable          执行的操作
     * @param lockFailOperation 获取锁失败时的操作
     */
    public static void waitLockAndExecute(RLock lock, String lockName, long time, TimeUnit unit, Runnable runnable, Runnable lockFailOperation) {
        if (runnable == null) {
            return;
        }
        boolean hasLocked = false;
        try {
            hasLocked = lock.tryLock(time, unit);
            if (hasLocked) {
                runnable.run();
            } else {
                if (lockFailOperation != null) {
                    lockFailOperation.run();
                }
            }
        } catch (Exception e) {
            String msg = String.format("Lock [%s] failed", lockName);
            logger.error(msg, e);
        } finally {
            if (hasLocked) {
                try {
                    lock.unlock();
                } catch (Exception e) {
                    String msg = String.format("Unlock [%s] failed", lockName);
                    logger.error(msg, e);
                }
            }
        }
    }

}
