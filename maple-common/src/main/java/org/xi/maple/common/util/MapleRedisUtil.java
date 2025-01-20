package org.xi.maple.common.util;

import org.xi.maple.common.model.MapleEngineExecutionQueue;

/**
 * @author xishihao
 */
public class MapleRedisUtil {

    /**
     * 获取用户组作业队列
     * 队列标识：clusterId + queue + 来源应用 + group + 优先级
     * 例：hadoop_prod-root.default-schedule-maple-1
     *
     * @param clusterId     集群
     * @param resourceGroup 集群资源组，如 YARN 队列、K8s Namespace 等
     * @param fromApp       来源应用
     * @param userGroup     用户组
     * @param priority      优先级
     * @return redis 队列信息
     */
    public static MapleEngineExecutionQueue getEngineExecutionQueue(
            Integer clusterId, String resourceGroup,
            String fromApp, Integer userGroup, Integer priority
    ) {
        String queueName = String.join("-", clusterId + "", resourceGroup, fromApp, userGroup + "", priority.toString());
        // megq (maple-execution-group-queue), megql (maple-execution-group-queue-lock)
        return new MapleEngineExecutionQueue("megq::" + queueName, clusterId, resourceGroup, fromApp, userGroup, priority);
    }
}
