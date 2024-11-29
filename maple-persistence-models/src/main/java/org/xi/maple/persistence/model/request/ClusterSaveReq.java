package org.xi.maple.persistence.model.request;

import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;

import javax.validation.constraints.*;

import lombok.Data;

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
    @NotNull(message = "configuration(集群配置)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String configuration;
}
