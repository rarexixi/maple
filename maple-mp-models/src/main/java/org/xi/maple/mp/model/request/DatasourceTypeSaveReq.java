package org.xi.maple.mp.model.request;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.mp.model.DatasourceConfOption;

import javax.validation.Valid;
import javax.validation.constraints.*;

import lombok.Data;

import java.util.List;

@Data
public class DatasourceTypeSaveReq extends BaseEntity {

    /**
     * 类型编码
     */
    @NotBlank(message = "typeCode(类型编码)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private String typeCode;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 图标地址
     */
    private String icon;

    /**
     * 分类
     */
    @NotBlank(message = "classifier(分类)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String classifier;

    /**
     * 版本(多个版本用","隔开)
     */
    private String versions;

    /**
     * 数据源配置信息
     */
    @NotBlank(message = "configurations(数据源配置信息)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String configurations;

    public void setConfigurations(@Valid DatasourceConfOption[] configurations) {
        this.configurations = JsonUtils.toJsonString(configurations, "[]");
    }
}
