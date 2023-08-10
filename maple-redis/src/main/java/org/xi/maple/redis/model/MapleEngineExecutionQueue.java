package org.xi.maple.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 作业 redis 队列
 *
 * @author xishihao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapleEngineExecutionQueue implements Serializable {

    private String queueName;
    private String lockName;
    private String cluster;
    private String clusterQueue;
    private String fromApp;
    private String group;
    private Integer priority;

    /**
     * redis 队列作业对象
     *
     * @author xishihao
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueueItem implements Serializable {

        private Integer execId;
        private Long timestamp;
    }
}