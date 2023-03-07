package org.xi.maple.datasource.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class BaseEntity implements Serializable {

    /**
     * 是否删除
     */
    protected Integer deleted;

    /**
     * 创建人
     */
    protected Integer createUser;

    /**
     * 修改人
     */
    protected Integer updateUser;

    /**
     * 创建时间
     */
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    protected LocalDateTime updateTime;
}
