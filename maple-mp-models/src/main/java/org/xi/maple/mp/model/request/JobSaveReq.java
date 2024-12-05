package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;

import javax.validation.constraints.*;

@Data
public class JobSaveReq extends BaseEntity {

    /**
     * 作业ID
     */
    @NotNull(message = "id(作业ID)不能为空", groups = {Jsr303ValidGroup.Put.class, Jsr303ValidGroup.Patch.class})
    private Integer id;

    /**
     * 作业名
     */
    private String jobName;

    /**
     * 作业说明
     */
    private String desc;

    /**
     * 作业类型
     */
    private String jobType;

    /**
     * 集群种类
     */
    private String clusterCategory;

    /**
     * 引擎种类
     */
    private String engineCategory;

    /**
     * 引擎版本
     */
    private String engineVersion;

    /**
     * 作业负责人
     */
    private String owner;

    /**
     * 执行内容
     */
    @NotBlank(message = "runContent(执行内容)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String runContent;

    /**
     * 作业配置
     */
    @NotBlank(message = "jobConf(作业配置)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String jobConf;
}
