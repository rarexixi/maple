package org.xi.maple.persistence.model.request;

import org.xi.maple.persistence.model.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.*;

import lombok.Data;

@Data
public class ClusterEngineDefaultConfAddRequest extends BaseEntity {

    /**
     * 引擎ID
     */
    private Integer id;

    /**
     * 主体类型
     */
    private String objType;

    /**
     * 所属主体
     */
    private String objName;

    /**
     * 集群引擎ID
     */
    @NotNull(message = "engineId(集群引擎ID)不能为空")
    private Integer engineId;

    /**
     * 默认配置
     */
    @NotNull(message = "defaultConf(默认配置)不能为空")
    private String defaultConf;
}
