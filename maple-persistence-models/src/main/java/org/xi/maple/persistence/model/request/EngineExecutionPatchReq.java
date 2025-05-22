package org.xi.maple.persistence.model.request;

import lombok.Data;
import org.xi.maple.common.model.BaseEntity;

import java.time.LocalDateTime;

@Data
public class EngineExecutionPatchReq extends BaseEntity {

    /**
     * 运行优先级
     */
    private Integer runPri;

    /**
     * 集群应用ID
     */
    private String clusterAppId;

    /**
     * 集群应用地址
     */
    private String clusterAppAddress;

    /**
     * 状态
     */
    private String status;

    /**
     * 任务提交时间
     */
    private LocalDateTime submittedAt;

    /**
     * 任务执行开始时间
     */
    private LocalDateTime startedAt;

    /**
     * 任务执行结束时间
     */
    private LocalDateTime finishedAt;
}
