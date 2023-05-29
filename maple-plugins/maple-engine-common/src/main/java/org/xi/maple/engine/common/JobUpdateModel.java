package org.xi.maple.engine.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 作业更新实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Data
@Accessors(chain = true)
public class JobUpdateModel implements Serializable {

    /**
     * 作业ID
     */
    private Integer id;

    /**
     * 执行内容类型 (text, path)
     */
    private String contentType;

    /**
     * 执行结果类型 (text, path)
     */
    private String resultType;

    /**
     * 执行结果
     */
    private String result;

    /**
     * 状态 (SUBMITTED, ACCEPTED, RUNNING, SUCCEED, FAILED, KILLED)
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
     * 作业配置
     */
    private String configuration;

    /**
     * 扩展信息
     */
    private String extInfo;
}
