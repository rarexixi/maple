package org.xi.maple.mp.model.response;

import lombok.Data;
import org.xi.maple.common.util.JsonUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ClusterEngineItemResp implements Serializable {

    /**
     * 引擎ID
     */
    private Integer id;

    /**
     * 所属集群
     */
    private String clusterId;

    /**
     * 引擎名称
     */
    private String name;

    /**
     * 引擎版本
     */
    private String version;

    /**
     * 引擎目录
     */
    private String engineHome;

    /**
     * 引擎配置
     */
    private String engineConf;

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

    public Map<String, Object> getEngineConf() {
        return (Map<String, Object>) JsonUtils.parseObject(engineConf, Map.class, Map.of());
    }
}
