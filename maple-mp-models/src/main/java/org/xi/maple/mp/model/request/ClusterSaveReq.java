package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.common.util.JsonUtils;

import javax.validation.constraints.*;
import java.util.Map;

@Data
public class ClusterSaveReq extends BaseEntity {

    /**
     * 集群名称
     */
    @NotBlank(message = "name(集群名称)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private String name;

    /**
     * 集群类型
     */
    @NotBlank(message = "category(集群类型)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String category;

    /**
     * 集群地址
     */
    @NotBlank(message = "address(集群地址)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String address;

    /**
     * 集群说明
     */
    private String desc;

    /**
     * 集群配置
     */
    @NotBlank(message = "configuration(集群配置)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String configuration;

    public void setConfiguration(Map<String, ?> configuration) {
        this.configuration = JsonUtils.toJsonString(configuration, "{}");
    }
}
