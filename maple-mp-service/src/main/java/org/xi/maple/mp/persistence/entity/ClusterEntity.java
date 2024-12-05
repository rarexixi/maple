package org.xi.maple.mp.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.xi.maple.common.model.BaseEntity;


/**
 * 集群实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ClusterEntity extends BaseEntity {

    /**
     * 集群ID
     */
    private Integer id;

    /**
     * 集群名称
     */
    private String name;

    /**
     * 集群种类
     */
    private String category;

    /**
     * 集群地址
     */
    private String address;

    /**
     * 集群说明
     */
    private String desc;

    /**
     * 集群配置
     */
    private String clusterConf;
}
