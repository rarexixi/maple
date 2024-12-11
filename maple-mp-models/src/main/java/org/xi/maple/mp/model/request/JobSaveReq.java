package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.annotation.Jsr303ValidGroup;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.common.util.JsonUtils;

import javax.validation.constraints.*;
import java.util.Map;

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
    @NotBlank(message = "jobName(作业名)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String jobName;

    /**
     * 作业说明
     */
    private String description;

    /**
     * 作业类型
     */
    @NotBlank(message = "jobType(作业类型)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String jobType;

    /**
     * 引擎ID
     */
    @NotNull(message = "engineId(引擎ID)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private Integer engineId;

    /**
     * 作业负责人
     */
    private String owner;

    /**
     * 执行配置
     */
    @NotBlank(message = "runConf(执行配置)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String runConf;

    /**
     * 作业配置
     */
    @NotBlank(message = "jobConf(作业配置)不能为空", groups = {Jsr303ValidGroup.Post.class, Jsr303ValidGroup.Put.class})
    private String jobConf;

    public void setRunConf(Map<String, ?> runConf) {
        this.runConf = JsonUtils.toJsonString(runConf, "{}");
    }

    public void setJobConf(Map<String, ?> jobConf) {
        this.jobConf = JsonUtils.toJsonString(jobConf, "{}");
    }
}
