package org.xi.maple.persistence.model.response;

import lombok.Data;

@Data
public class EngineExecutionDetailResp extends EngineExecutionItemResp {
    /**
     * 集群类型
     */
    private String clusterCategory;

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
    private String execInfo;
}
