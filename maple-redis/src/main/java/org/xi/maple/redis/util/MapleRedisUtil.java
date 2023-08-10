package org.xi.maple.redis.util;

import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.redis.model.MapleEngineExecutionQueue;
import org.xi.maple.redis.model.MapleJobQueue;

import java.util.concurrent.TimeUnit;

/**
 * @author xishihao
 */
public class MapleRedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(MapleRedisUtil.class);

    /**
     * 获取用户组作业队列
     * 队列标识：cluster + queue + 来源应用 + group + 优先级
     * 例：hadoop_prod-root.default-schedule-maple-1
     *
     * @param cluster        集群
     * @param clusterQueue   集群队列
     * @param fromApp        来源应用
     * @param group          用户组
     * @param priority       优先级
     * @return redis 队列信息
     */
    public static MapleEngineExecutionQueue getEngineExecutionQueue(
            String cluster, String clusterQueue,
            String fromApp, String group, Integer priority
    ) {
        String queueName = String.join("-", cluster, clusterQueue, fromApp, group, priority.toString());
        // megq (maple-execution-group-queue), megql (maple-execution-group-queue-lock)
        return new MapleEngineExecutionQueue("megq::" + queueName, "megql::" + queueName,
                cluster, clusterQueue, fromApp, group, priority);
    }

    /**
     * 获取用户组作业队列
     * 队列标识：cluster + queue + 引擎种类 + 引擎版本 + 来源应用 + group + 优先级
     * 例：hadoop_prod-root.default-spark-3.2.3-once-schedule-maple-1
     *
     * @param cluster        集群
     * @param clusterQueue   集群队列
     * @param engineCategory 引擎种类
     * @param engineVersion  引擎版本
     * @param fromApp        来源应用
     * @param group          用户组
     * @param priority       优先级
     * @return redis 队列信息
     */
    public static MapleJobQueue getJobQueue(
            String cluster, String clusterQueue,
            String engineCategory, String engineVersion,
            String fromApp, String group, Integer priority
    ) {
        String queueName = String.join("-", cluster, clusterQueue, engineCategory, engineVersion, fromApp, group, priority.toString());
        // mgq (maple-group-queue), mgql (maple-group-queue-lock)
        return new MapleJobQueue("mgq::" + queueName, "mgql::" + queueName,
                cluster, clusterQueue, engineCategory, engineVersion, fromApp, group, priority);
    }

    /**
     * 获取引擎锁
     *
     * @param engineInstanceId 引擎ID
     * @return redis 引擎实例锁的 key
     */
    public static String getEngineInstanceLock(Integer engineInstanceId) {
        // meil (maple-engine-instance-lock)
        return "meil::" + engineInstanceId;
    }

    /**
     * 获取引擎锁
     *
     * @param cluster        集群
     * @param clusterQueue   集群队列
     * @param engineCategory 引擎种类
     * @param engineVersion  引擎版本
     * @param fromApp        来源应用
     * @param group          用户组
     * @return redis 引擎锁的 key
     */
    public static String getEngineLock(
            String cluster, String clusterQueue,
            String engineCategory, String engineVersion,
            String fromApp, String group
    ) {
        String queueName = String.join("-", cluster, clusterQueue, engineCategory, engineVersion, fromApp, group);
        // mel (maple-engine-lock)
        return "mel::" + queueName;
    }

    /**
     * 尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock     锁
     * @param lockName 锁的名字
     * @param runnable 执行的操作
     */
    public static void tryLockAndExecute(RLock lock, String lockName, Runnable runnable) {
        waitLockAndExecute(lock, lockName, 0, 2, TimeUnit.SECONDS, runnable, null);
    }

    /**
     * 尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock      锁
     * @param lockName  锁的名字
     * @param leaseTime 锁超时释放时长（秒）
     * @param runnable  执行的操作
     */
    public static void tryLockAndExecute(RLock lock, String lockName, long leaseTime, Runnable runnable) {
        waitLockAndExecute(lock, lockName, 0, leaseTime, TimeUnit.SECONDS, runnable, null);
    }

    /**
     * 尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock      锁
     * @param lockName  锁的名字
     * @param leaseTime 锁超时释放时长
     * @param unit      时间单位
     * @param runnable  执行的操作
     */
    public static void tryLockAndExecute(RLock lock, String lockName, long leaseTime, TimeUnit unit, Runnable runnable) {
        waitLockAndExecute(lock, lockName, 0, leaseTime, unit, runnable, null);
    }

    /**
     * 尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock              锁
     * @param lockName          锁的名字
     * @param leaseTime         锁超时释放时长
     * @param unit              时间单位
     * @param runnable          执行的操作
     * @param lockFailOperation 获取锁失败时的操作
     */
    public static void tryLockAndExecute(RLock lock, String lockName,
                                         long leaseTime, TimeUnit unit,
                                         Runnable runnable, Runnable lockFailOperation
    ) {
        waitLockAndExecute(lock, lockName, 0, leaseTime, unit, runnable, lockFailOperation);
    }

    /**
     * 在尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock      锁
     * @param lockName  锁的名字
     * @param time      等待锁的最大时长（秒）
     * @param leaseTime 锁超时释放时长
     * @param runnable  执行的操作
     */
    public static void waitLockAndExecute(RLock lock, String lockName, long time, long leaseTime, Runnable runnable) {
        waitLockAndExecute(lock, lockName, time, leaseTime, TimeUnit.SECONDS, runnable, null);
    }

    /**
     * 在尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock              锁
     * @param lockName          锁的名字
     * @param time              等待锁的最大时长
     * @param leaseTime         锁超时释放时长
     * @param runnable          执行的操作
     * @param lockFailOperation 获取锁失败时的操作
     */
    public static void waitLockAndExecute(
            RLock lock, String lockName,
            long time, long leaseTime,
            Runnable runnable, Runnable lockFailOperation
    ) {
        waitLockAndExecute(lock, lockName, time, leaseTime, TimeUnit.SECONDS, runnable, lockFailOperation);
    }

    /**
     * 在尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock      锁
     * @param lockName  锁的名字
     * @param time      等待锁的最大时长
     * @param leaseTime 锁超时释放时长
     * @param unit      时间单位
     * @param runnable  执行的操作
     */
    public static void waitLockAndExecute(
            RLock lock, String lockName,
            long time, long leaseTime, TimeUnit unit,
            Runnable runnable
    ) {
        waitLockAndExecute(lock, lockName, time, leaseTime, unit, runnable, null);
    }

    /**
     * 尝试获取锁，并执行，获取不到锁直接跳过
     *
     * @param lock              锁
     * @param lockName          锁的名字
     * @param time              等待锁的最大时长
     * @param leaseTime         锁超时释放时长
     * @param unit              时间单位
     * @param runnable          执行的操作
     * @param lockFailOperation 获取锁失败时的操作
     */
    public static void waitLockAndExecute(
            RLock lock, String lockName,
            long time, long leaseTime, TimeUnit unit,
            Runnable runnable, Runnable lockFailOperation
    ) {
        if (runnable == null) {
            return;
        }
        boolean hasLocked = false;
        try {
            hasLocked = lock.tryLock(time, leaseTime, unit);
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
