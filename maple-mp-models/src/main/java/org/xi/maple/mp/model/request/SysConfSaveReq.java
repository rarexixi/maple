package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;

import javax.validation.constraints.*;

@Data
public class SysConfSaveReq extends BaseEntity {

    /**
     * 配置键
     */
    @NotBlank(message = "confKey(配置键)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private String confKey;

    /**
     * 配置值
     */
    @NotBlank(message = "confValue(配置值)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String confValue;

    /**
     * 配置说明
     */
    private String description;
}
