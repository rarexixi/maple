package org.xi.maple.persistence.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xi.maple.persistence.model.BaseEntity;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EngineExecutionUpdateStatusRequest extends BaseEntity {

    /**
     * 执行ID
     */
    @NotNull(message = "id(执行ID)不能为空")
    private Integer id;

    /**
     * 状态 (SUBMITTED, ACCEPTED, RUNNING, SUCCEED, FAILED, KILLED)
     */
    private String status;
}
