package org.xi.maple.persistence.model.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class ClusterEngineDefaultConfItemResp implements Serializable {

    /**
     * 引擎ID
     */
    private Integer id;

    /**
     * 主体类型
     */
    private String objType;

    /**
     * 所属主体
     */
    private String objName;

    /**
     * 集群引擎ID
     */
    private Integer engineId;

    /**
     * 默认配置
     */
    private String defaultConf;
}
