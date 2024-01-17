package org.xi.maple.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xi.maple.common.model.MapleEngineExecutionQueue;
import org.xi.maple.common.model.MapleJobQueue;

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
     * @param cluster      集群
     * @param clusterQueue 集群队列
     * @param fromApp      来源应用
     * @param group        用户组
     * @param priority     优先级
     * @return redis 队列信息
     */
    public static MapleEngineExecutionQueue getEngineExecutionQueue(
            String cluster, String clusterQueue,
            String fromApp, String group, Integer priority
    ) {
        String queueName = String.join("-", cluster, clusterQueue, fromApp, group, priority.toString());
        // megq (maple-execution-group-queue), megql (maple-execution-group-queue-lock)
        return new MapleEngineExecutionQueue("megq::" + queueName, cluster, clusterQueue, fromApp, group, priority);
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
}
