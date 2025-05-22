package org.xi.maple.executor.builder;

import org.xi.maple.builder.model.EngineExecutionModel;

public interface EngineExecutor {

    /**
     * 执行引擎作业
     *
     * @param execution 执行实体对象
     */
    void execute(EngineExecutionModel execution) throws Exception;

    /**
     * 操作引擎作业
     *
     * @param action 操作实体对象
     */
    void operate(EngineExecutionModel action) throws Exception;
}
