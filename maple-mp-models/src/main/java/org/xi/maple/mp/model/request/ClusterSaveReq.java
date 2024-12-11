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
     * 集群ID
     */
    @NotNull(message = "id(集群ID)不能为空", groups = {Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private Integer id;

    /**
     * 集群名称
     */
    @NotBlank(message = "name(集群名称)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String name;

    /**
     * 集群种类
     */
    @NotBlank(message = "category(集群种类)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String category;

    /**
     * 集群地址
     */
    @NotBlank(message = "address(集群地址)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String address;

    /**
     * 集群说明
     */
    private String description;

    /**
     * 集群配置
     */
    @NotBlank(message = "clusterConf(集群配置)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String clusterConf;

    public void setClusterConf(Map<String, ?> clusterConf) {
        this.clusterConf = JsonUtils.toJsonString(clusterConf, "{}");
    }
}
