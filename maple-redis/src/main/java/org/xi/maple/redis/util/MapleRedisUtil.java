package org.xi.maple.redis.util;

import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.redis.model.MapleJobQueue;

import java.util.concurrent.TimeUnit;

/**
 * @author xishihao
 */
public class MapleRedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(MapleRedisUtil.class);

    public static final String JOB_QUEUE_CACHE_NAME = "mq";

    /**
     * 获取用户组作业队列
     *
     * @return redis 队列名
     */
    public static MapleJobQueue getJobQueue(
            String cluster,
            String clusterQueue,
            String fromApp,
            String jobType,
            String group,
            Integer priority
    ) {
        String queueName = String.format("%s-%s-%s-%s", fromApp, jobType, group, priority);
        // mgq (maple-group-queue), mgql (maple-group-queue-lock)
        return new MapleJobQueue("mgq::" + queueName, "mgql::" + queueName, cluster, clusterQueue, jobType);
    }

    /**
     * 获取用户作业队列
     *
     * @return redis 队列名
     */
    public static MapleJobQueue getUserJobQueue(
            String cluster,
            String clusterQueue,
            String fromApp,
            String jobType,
            String user,
            Integer priority
    ) {
        String queueName = String.format("%s-%s-%s-%s", fromApp, jobType, user, priority);
        // muq (maple-user-queue), muql (maple-user-queue-lock)
        return new MapleJobQueue("muq::" + queueName, "muql::" + queueName, cluster, clusterQueue, jobType);
    }

    /**
     * 获取引擎锁
     *
     * @param engineInstanceId 引擎ID
     * @return redis 引擎锁的 key
     */
    public static String getEngineInstanceLock(Integer engineInstanceId) {
        // mel (maple-engine-lock)
        return String.format("mel::%s", engineInstanceId);
    }

    /**
     * 尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock     锁
     * @param lockName 锁的名字
     * @param runnable 执行的操作
     */
    public static void tryLockAndExecute(RLock lock, String lockName, Runnable runnable) {
        tryLockAndExecute(lock, lockName, runnable, null);
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
