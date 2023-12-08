package org.xi.maple.persistence.model.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class ApplicationListItemResponse implements Serializable {

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
    private String callbackUrls;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 修改人
     */
    private Integer updateUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
