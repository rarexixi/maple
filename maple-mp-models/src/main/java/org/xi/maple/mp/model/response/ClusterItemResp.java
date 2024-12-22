package org.xi.maple.mp.model.response;

import lombok.Data;
import org.xi.maple.common.util.JsonUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Data
public class ClusterItemResp implements Serializable {

    /**
     * 集群ID
     */
    private Integer id;

    /**
     * 集群名称
     */
    private String name;

    /**
     * 集群种类
     */
    private String category;

    /**
     * 集群地址
     */
    private String address;

    /**
     * 集群说明
     */
    private String description;

    /**
     * 集群配置
     */
    private String clusterConf;

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

    public Map<String, Object> getClusterConf() {
        return (Map<String, Object>) JsonUtils.parseObject(clusterConf, Map.class, Collections.emptyMap());
    }
}
