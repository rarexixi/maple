package org.xi.maple.common.model;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.common.annotation.SetField;

import static org.xi.maple.common.constant.SetFieldType.*;

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
     * 是否禁用
     */
    @SetField(types = {CREATE, UPDATE}, defaultValue = "#{0}", force = false)
    protected Integer disabled;

    /**
     * 创建人
     */
    @SetField(field = "id", types = {CREATE})
    protected Integer createdBy;

    /**
     * 修改人
     */
    @SetField(field = "id", types = {CREATE, UPDATE})
    protected Integer updatedBy;

    /**
     * 创建时间
     */
    @SetField(types = {CREATE}, defaultValue = "#{T(java.time.LocalDateTime).now()}")
    protected LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @SetField(types = {CREATE, UPDATE}, defaultValue = "#{T(java.time.LocalDateTime).now()}")
    protected LocalDateTime updatedAt;
}
