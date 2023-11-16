package org.xi.maple.persistence.model.request;

import org.xi.maple.persistence.model.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.*;

import lombok.Data;

@Data
public class ClusterAddRequest extends BaseEntity {

    /**
     * 集群名称
     */
    @NotBlank(message = "name(集群名称)不能为空")
    private String name;

    /**
     * 集群类型
     */
    @NotBlank(message = "category(集群类型)不能为空")
    private String category;

    /**
     * 集群地址
     */
    @NotBlank(message = "address(集群地址)不能为空")
    private String address;

    /**
     * 集群说明
     */
    private String desc;

    /**
     * 集群配置
     */
    @NotNull(message = "configuration(集群配置)不能为空")
    private String configuration;
}
