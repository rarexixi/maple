package org.xi.maple.persistence.model.request;

import lombok.Data;
import org.xi.maple.persistence.model.BaseEntity;

import javax.validation.constraints.NotNull;

@Data
public class ClusterEngineDefaultConfSaveRequest extends BaseEntity {

    /**
     * 引擎ID
     */
    @NotNull(message = "id(引擎ID)不能为空")
    private Integer id;

    /**
     * 默认配置
     */
    private String defaultConf;
}
