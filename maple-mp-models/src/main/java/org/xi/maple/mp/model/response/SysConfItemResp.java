package org.xi.maple.mp.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysConfItemResp implements Serializable {

    /**
     * 配置键
     */
    private String confKey;

    /**
     * 配置值
     */
    private String confValue;

    /**
     * 配置说明
     */
    private String desc;

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
