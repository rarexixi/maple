package org.xi.maple.mp.model.response;

import lombok.Data;

import org.xi.maple.common.util.JsonUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DatasourceTypeItemResp implements Serializable {

    /**
     * 类型编码
     */
    private String typeCode;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 图标地址
     */
    private String icon;

    /**
     * 分类
     */
    private String classifier;

    /**
     * 版本(多个版本用","隔开)
     */
    private String versions;

    /**
     * 数据源配置信息
     */
    private String configurations;

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

    public Object[] getConfigurations() {
        return JsonUtils.parseObject(configurations, Object[].class, new Object[0]);
    }
}
