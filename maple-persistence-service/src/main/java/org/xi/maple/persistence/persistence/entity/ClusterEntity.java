package org.xi.maple.persistence.persistence.entity;

import org.xi.maple.persistence.model.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
     * 集群名称
     */
    private String name;

    /**
     * 集群类型
     */
    private String type;

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
    private String configuration;
}
