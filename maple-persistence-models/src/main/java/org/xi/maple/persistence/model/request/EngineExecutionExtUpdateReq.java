package org.xi.maple.persistence.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xi.maple.common.model.BaseEntity;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EngineExecutionExtUpdateReq extends BaseEntity {

    /**
     * 执行ID
     */
    @NotNull(message = "id(执行ID)不能为空")
    private Integer id;

    /**
     * 执行信息
     */
    private String execInfo; // todo
}
