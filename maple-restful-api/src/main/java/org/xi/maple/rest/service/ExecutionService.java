package org.xi.maple.rest.service;

import org.xi.maple.persistence.model.request.EngineExecutionSaveReq;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;
import org.xi.maple.rest.model.request.ExecReq;
import org.xi.maple.rest.model.request.JobExecReq;

import java.util.Map;

/**
 * @author xishihao
 */
public interface ExecutionService {

    /**
     * 获取执行状态
     *
     * @param id 执行记录ID
     * @return 作业状态
     */
    String getExecutionStatus(Integer id);

    /**
     * 获取执行详情
     *
     * @param id 执行记录ID
     * @return 作业详情
     */
    EngineExecutionDetailResp detail(Integer id);

    /**
     * 提交执行
     *
     * @param execReq 执行提交请求对象
     * @return 执行记录ID
     */
    Integer submit(ExecReq execReq);

    /**
     * 提交执行
     *
     * @param jobExecReq 执行提交请求对象
     * @return 执行记录ID
     */
    Integer submitJob(JobExecReq jobExecReq);

    /**
     * 立即执行，返回提交结果
     *
     * @param execReq 执行提交请求对象
     * @return 执行记录ID
     */
    Integer exec(ExecReq execReq);

    /**
     * 杀死执行任务
     * @param id 执行记录ID
     * @param app 来源应用
     * @return 执行结果
     */
    Object kill(Integer id, String app);

    /**
     * 取消执行任务
     * @param id 执行记录ID
     * @param app 来源应用
     * @return 执行结果
     */
    Object stop(Integer id, Map<String, ?> cancelParams, String app);
}
