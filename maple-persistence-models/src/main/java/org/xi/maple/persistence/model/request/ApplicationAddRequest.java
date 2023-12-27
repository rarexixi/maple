package org.xi.maple.persistence.model.request;

import org.xi.maple.persistence.model.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.*;

import lombok.Data;

@Data
public class ApplicationAddRequest extends BaseEntity {

    /**
     * 应用名称
     */
    @NotBlank(message = "appName(应用名称)不能为空")
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
}
