package org.xi.maple.mp.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.xi.maple.common.model.BaseEntity;


/**
 * 系统配置实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class SysConfEntity extends BaseEntity {

    /**
     * 配置键
     */
    private String confKey;

    /**
     * 配置值
     */
    private String confValue;

    /**
     * 配置说明
     */
    private String desc;
}
