package org.xi.maple.scheduler.persistence.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 执行器实例实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Data
public class EngineInstanceEntity implements Serializable {

    /**
     * 引擎ID
     */
    private Integer id;

    /**
     * 程序ID
     */
    private String applicationId;

    /**
     * 请求集群
     */
    private String cluster;

    /**
     * 集群队列
     */
    private String clusterQueue;

    /**
     * 地址
     */
    private String address;

    /**
     * 引擎种类
     */
    private String engineCategory;

    /**
     * 版本
     */
    private String engineVersion;

    /**
     * 引擎类型 (once，permanent)
     */
    private String engineType;

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

    /**
     * 是否已清理作业
     */
    private Integer jobCleaned;

    /**
     * 用户组
     */
    private String group;

    /**
     * 用户
     */
    private String user;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
