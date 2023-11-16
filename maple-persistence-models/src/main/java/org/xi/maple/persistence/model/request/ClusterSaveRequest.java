package org.xi.maple.persistence.model.request;

import org.xi.maple.persistence.model.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.*;

import lombok.Data;

@Data
public class ClusterSaveRequest extends BaseEntity {

    /**
     * 集群名称
     */
    private String name;

    /**
     * 集群类型
     */
    private String category;

    /**
     * 集群地址
     */
    private String address;

    /**
     * 集群说明
     */
    private String desc;

    /**
     * 集群配置
     */
    private String configuration;
}
