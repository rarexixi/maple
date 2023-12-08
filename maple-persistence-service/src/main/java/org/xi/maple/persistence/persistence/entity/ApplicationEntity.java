package org.xi.maple.persistence.persistence.entity;

import org.xi.maple.persistence.model.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 访问程序实体
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Getter
@Setter
@ToString
public class ApplicationEntity extends BaseEntity {

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
}
