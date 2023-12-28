package org.xi.maple.persistence.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EngineExecutionUpdateStatusRequest {

    public EngineExecutionUpdateStatusRequest(String status) {
        this.status = status;
    }

    public EngineExecutionUpdateStatusRequest(String status, String rawStatus) {
        this.status = status;
        this.rawStatus = rawStatus;
    }

    /**
     * 状态
     */
    private String status;

    /**
     * 原始状态
     */
    private String rawStatus = "";

    /**
     * 状态码
     */
    private Integer statusCode = 0;

    /**
     * 状态说明
     */
    private String statusMsg = "";
}
