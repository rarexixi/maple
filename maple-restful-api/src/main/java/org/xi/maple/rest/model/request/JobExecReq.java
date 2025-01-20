package org.xi.maple.rest.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class JobExecReq implements Serializable {

    /**
     * 作业ID
     */
    @NotNull(message = "jobId(作业ID)不能为空")
    private Integer jobId;

    /**
     * 执行批次ID
     */
    @NotBlank(message = "bizId(执行批次ID)不能为空")
    private String bizId;

    /**
     * 执行名称
     */
    private String execName;

    /**
     * 用户组
     */
    @NotNull(message = "userGroup(用户组)不能为空")
    private Integer userGroup;

    /**
     * 执行人
     */
    @NotNull(message = "runBy(执行人)不能为空")
    private Integer runBy;
}
