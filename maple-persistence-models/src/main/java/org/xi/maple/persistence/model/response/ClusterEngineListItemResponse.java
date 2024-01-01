package org.xi.maple.persistence.model.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class ClusterEngineListItemResponse implements Serializable {

    /**
     * 引擎ID
     */
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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
