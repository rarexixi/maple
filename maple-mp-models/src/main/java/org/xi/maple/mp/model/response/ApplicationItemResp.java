package org.xi.maple.mp.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ApplicationItemResp implements Serializable {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用访问密钥
     */
    private String accessKey;

    /**
     * 允许请求的IP
     */
    private String legalHosts;

    /**
     * 回调接口
     */
    private String webhooks;

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
