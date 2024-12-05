package org.xi.maple.mp.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.xi.maple.common.model.BaseEntity;


/**
 * 计算引擎实体
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
     * 所属集群
     */
    private String cluster;

    /**
     * 引擎名称
     */
    private String name;

    /**
     * 引擎版本
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
