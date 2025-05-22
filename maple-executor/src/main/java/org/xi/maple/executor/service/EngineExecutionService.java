package org.xi.maple.executor.service;

import org.xi.maple.persistence.model.response.EngineExecutionAction;

public interface EngineExecutionService {

    /**
     * 执行引擎作业
     *
     * @param execution 执行实体对象
     */
    void execute(EngineExecutionAction execution) throws Exception;

    /**
     * 操作引擎作业
     *
     * @param execution 执行实体对象
     */
    void operate(EngineExecutionAction execution) throws Exception;
}
