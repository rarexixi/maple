package org.xi.maple.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 执行作业实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class JobEntity {

    /**
     * 作业ID
     */
    private Integer id;

    /**
     * 作业名
     */
    private String jobName;

    /**
     * 来源应用
     */
    private String fromApp;

    /**
     * 唯一ID
     */
    private String uniqueId;

    /**
     * 作业说明
     */
    private String jobComment;

    /**
     * 引擎ID
     */
    private String engineId;

    /**
     * 引擎类型
     */
    private String engineType;

    /**
     * 版本
     */
    private String engineVersion;

    /**
     * 作业类型 (sync, async)
     */
    private String jobType;

    /**
     * 执行内容类型 (text, path)
     */
    private String contentType;

    /**
     * 执行结果类型 (text, path)
     */
    private String resultType;

    /**
     * 状态 (SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED, KILLED)
     */
    private String status;

    /**
     * 用户组
     */
    private String group;

    /**
     * 用户
     */
    private String user;

    /**
     * 回调地址
     */
    private String webhooks;

    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 创建时间
     */
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    protected LocalDateTime updateTime;
}
