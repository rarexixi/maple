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
public class MapleEngineExecutionQueue implements Serializable {

    private String queueName;
    private Integer cluster;
    private String resourceGroup;
    private String fromApp;
    private Integer userGroup;
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

        private Integer execId;
        private Long timestamp;
    }
}