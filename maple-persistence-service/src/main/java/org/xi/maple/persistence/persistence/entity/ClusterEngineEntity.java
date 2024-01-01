package org.xi.maple.persistence.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.persistence.model.BaseEntity;

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
     * 集群名称
     */
    private String cluster;

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
     * 扩展信息
     */
    private String extInfo;
}
