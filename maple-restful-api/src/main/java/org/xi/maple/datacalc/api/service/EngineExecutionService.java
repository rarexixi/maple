package org.xi.maple.datacalc.api.service;

import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;

/**
 * @author xishihao
 */
public interface EngineExecutionService {

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
    Integer submit(EngineExecutionAddRequest submitReq, String timestamp, String secret);
}
