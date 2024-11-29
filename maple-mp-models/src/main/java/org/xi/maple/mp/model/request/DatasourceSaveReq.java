package org.xi.maple.mp.model.request;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.common.util.JsonUtils;

import java.util.Map;
import javax.validation.constraints.*;

import lombok.Data;

@Data
public class DatasourceSaveReq extends BaseEntity {

    /**
     * Id
     */
    @NotNull(message = "id(Id)不能为空", groups = {Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private Integer id;

    /**
     * 数据源名称
     */
    @NotBlank(message = "name(数据源名称)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String name;

    /**
     * 数据源描述
     */
    private String description;

    /**
     * 数据源类型
     */
    @NotBlank(message = "datasourceType(数据源类型)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String datasourceType;

    /**
     * 数据源版本
     */
    @NotBlank(message = "version(数据源版本)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String version;

    /**
     * 数据源配置
     */
    @NotBlank(message = "datasourceConf(数据源配置)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String datasourceConf;

    public void setDatasourceConf(Map<String, String> datasourceConf) {
        this.datasourceConf = JsonUtils.toJsonString(datasourceConf, "{}");
    }
}
