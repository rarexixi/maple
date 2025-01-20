package org.xi.maple.persistence.model.request;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class ClusterEngineDefaultConfSaveReq extends BaseEntity {

    /**
     * 引擎ID
     */
    @NotNull(message = "id(引擎ID)不能为空", groups = {Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private Integer id;

    /**
     * 主体类型
     */
    @Null(message = "id(引擎ID)必须为空", groups = {Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private String objType;

    /**
     * 所属主体
     */
    @Null(message = "id(引擎ID)必须为空", groups = {Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private Integer objId;

    /**
     * 集群引擎ID
     */
    @NotNull(message = "engineId(集群引擎ID)不能为空", groups = {Jsr303ValidGroup.Post.class})
    private Integer engineId;

    /**
     * 默认配置
     */
    @NotNull(message = "defaultConf(默认配置)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String defaultConf;
}
