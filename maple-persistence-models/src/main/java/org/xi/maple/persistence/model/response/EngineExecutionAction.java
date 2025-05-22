package org.xi.maple.persistence.model.response;

import lombok.Data;

import java.util.Map;

@Data
public class EngineExecutionAction extends EngineExecutionDetailResp {

    /**
     * 操作名称，如 stop，cancel 等
     */
    private String action;

    /**
     * 执行操作所需要的参数
     */
    private Map<String, ?> params;
}
