package org.xi.maple.engine.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 引擎更新实体
 *
 * @author xishihao
 */
@Data
@Accessors(chain = true)
public class EngineUpdateModel {

    /**
     * 引擎ID
     */
    private Integer id;

    /**
     * 程序ID
     */
    private String applicationId;

    /**
     * 地址
     */
    private String address;

    /**
     * 执行的作业次数
     */
    private Integer jobCount;

    /**
     * 执行中的作业数量
     */
    private Integer runningCount;

    /**
     * 心跳时间
     */
    private LocalDateTime heartbeatTime;

    /**
     * 状态 (SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED, KILLED)
     */
    private String status;
}
