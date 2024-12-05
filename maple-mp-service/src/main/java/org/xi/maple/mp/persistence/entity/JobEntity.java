package org.xi.maple.mp.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.xi.maple.common.model.BaseEntity;


/**
 * 执行作业实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class JobEntity extends BaseEntity {

    /**
     * 作业ID
     */
    private Integer id;

    /**
     * 作业名
     */
    private String jobName;

    /**
     * 作业说明
     */
    private String desc;

    /**
     * 作业类型
     */
    private String jobType;

    /**
     * 集群种类
     */
    private String clusterCategory;

    /**
     * 引擎种类
     */
    private String engineCategory;

    /**
     * 引擎版本
     */
    private String engineVersion;

    /**
     * 作业负责人
     */
    private String owner;

    /**
     * 执行内容
     */
    private String runContent;

    /**
     * 作业配置
     */
    private String jobConf;
}
