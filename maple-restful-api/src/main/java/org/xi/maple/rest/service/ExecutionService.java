package org.xi.maple.rest.service;

import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

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
    EngineExecutionDetailResponse detail(Integer id);

    /**
     * 提交执行
     *
     * @param submitReq 执行提交请求对象
     * @return 执行记录ID
     */
    Integer submit(EngineExecutionAddRequest submitReq);

    /**
     * 立即执行，返回提交结果
     *
     * @param submitReq 执行提交请求对象
     * @return 执行记录ID
     */
    Integer submitNow(EngineExecutionAddRequest submitReq);

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
