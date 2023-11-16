package org.xi.maple.persistence.model.response;

import lombok.Data;

@Data
public class EngineExecutionDetailResponse extends EngineExecutionListItemResponse {

    /**
     * 集群类型
     */
    private String clusterCategory;

    /**
     * 执行内容
     */
    private String execContent;

    /**
     * 作业配置
     */
    private String configuration;

    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 执行信息
     */
    private String processInfo;
}
