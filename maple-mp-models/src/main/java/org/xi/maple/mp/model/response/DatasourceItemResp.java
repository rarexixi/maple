package org.xi.maple.mp.model.response;

import lombok.Data;

import org.xi.maple.common.util.JsonUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class DatasourceItemResp implements Serializable {

    /**
     * Id
     */
    private Integer id;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源描述
     */
    private String description;

    /**
     * 数据源类型
     */
    private String datasourceType;

    /**
     * 数据源版本
     */
    private String version;

    /**
     * 数据源配置
     */
    private String datasourceConf;

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

    public Map<String, Object> getDatasourceConf() {
        return (Map<String, Object>) JsonUtils.parseObject(datasourceConf, Map.class, new HashMap<String, Object>());
    }
}
