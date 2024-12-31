package org.xi.maple.persistence.persistence.entity;

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
    private String description;

    /**
     * 作业类型
     */
    private String jobType;

    /**
     * 引擎ID
     */
    private Integer engineId;

    /**
     * 作业负责人
     */
    private String owner;

    /**
     * 执行配置
     */
    private String runConf;

    /**
     * 作业配置
     */
    private String jobConf;
}
