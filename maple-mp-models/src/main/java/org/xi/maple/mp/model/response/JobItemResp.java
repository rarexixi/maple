package org.xi.maple.mp.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class JobItemResp implements Serializable {

    /**
     * 作业ID
     */
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
    private String runContent;

    /**
     * 作业配置
     */
    private String jobConf;

    /**
     * 是否禁用
     */
    private Integer disabled;

    /**
     * 创建人
     */
    private Integer createdBy;

    /**
     * 修改人
     */
    private Integer updatedBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
