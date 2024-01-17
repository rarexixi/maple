package org.xi.maple.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 作业 redis 队列
 *
 * @author xishihao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MapleJobQueue implements Serializable {

    private String queueName;
    private String lockName;
    private String cluster;
    private String clusterQueue;
    private String engineCategory;
    private String engineVersion;
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
    @ToString
    public static class QueueItem implements Serializable {

        private Integer jobId;
        private Long timestamp;
    }
}