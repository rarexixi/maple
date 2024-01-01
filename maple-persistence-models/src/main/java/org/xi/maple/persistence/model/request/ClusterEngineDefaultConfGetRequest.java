package org.xi.maple.persistence.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClusterEngineDefaultConfGetRequest {

    /**
     * 集群名称
     */
    private String cluster;

    /**
     * 引擎类型
     */
    private String engine;

    /**
     * 引擎版本
     */
    private String version;

    /**
     * 用户组
     */
    private String userGroup;

    /**
     * 用户
     */
    private String user;
}
