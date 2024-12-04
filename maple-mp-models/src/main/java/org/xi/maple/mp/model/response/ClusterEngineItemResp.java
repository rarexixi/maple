package org.xi.maple.mp.model.response;

import lombok.Data;
import org.xi.maple.common.util.JsonUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class ClusterEngineItemResp implements Serializable {

    /**
     * 引擎ID
     */
    private Integer id;

    /**
     * 集群名称
     */
    private String cluster;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型版本
     */
    private String version;

    /**
     * 引擎目录
     */
    private String engineHome;

    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    public Map<String, Object> getExtInfo() {
        return (Map<String, Object>) JsonUtils.parseObject(extInfo, Map.class, Map.of());
    }
}
