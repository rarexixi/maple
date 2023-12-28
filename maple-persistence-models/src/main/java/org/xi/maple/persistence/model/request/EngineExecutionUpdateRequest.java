package org.xi.maple.persistence.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xi.maple.persistence.model.BaseEntity;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EngineExecutionUpdateRequest extends BaseEntity {

    /**
     * 执行内容
     */
    private String execContent;

    /**
     * 作业配置
     */
    private String configuration;

    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 执行信息
     */
    private String processInfo;
}
