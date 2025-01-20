package org.xi.maple.persistence.persistence.entity;

import org.xi.maple.common.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 集群引擎实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ClusterEngineEntity extends BaseEntity {

    /**
     * 引擎ID
     */
    private Integer id;

    /**
     * 集群ID
     */
    private Integer clusterId;

    /**
     * 集群名称
     */
    private Integer clusterName;

    /**
     * 集群类型
     */
    private String clusterCategory;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型版本
     */
    private String version;

    /**
     * 引擎目录
     */
    private String engineHome;

    /**
     * 引擎配置
     */
    private String engineConf;
}
