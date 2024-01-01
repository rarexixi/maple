package org.xi.maple.persistence.model.request;

import org.xi.maple.persistence.model.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.validation.constraints.*;

import lombok.Data;

@Data
public class ClusterEngineSaveRequest extends BaseEntity {

    /**
     * 引擎ID
     */
    @NotNull(message = "id(引擎ID)不能为空")
    private Integer id;

    /**
     * 集群名称
     */
    private String cluster;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型版本
     */
    private String version;

    /**
     * 引擎目录
     */
    private String engineHome;

    /**
     * 扩展信息
     */
    private String extInfo;
}
