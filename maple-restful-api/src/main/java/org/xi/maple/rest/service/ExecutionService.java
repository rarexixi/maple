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
     * @param timestamp 时间戳
     * @param secret    加密字符串
     * @return 执行记录ID
     */
    Integer submit(EngineExecutionAddRequest submitReq, Long timestamp, String secret);

    /**
     * 立即执行，返回提交结果
     *
     * @param submitReq 执行提交请求对象
     * @param timestamp 时间戳
     * @param secret    加密字符串
     * @return 执行记录ID
     */
    Integer submitNow(EngineExecutionAddRequest submitReq, Long timestamp, String secret);

    /**
     * 杀死执行任务
     * @param id 执行记录ID
     * @param timestamp 时间戳
     * @param secret 加密字符串
     * @return 执行结果
     */
    Object kill(Integer id, Long timestamp, String secret);

    /**
     * 取消执行任务
     * @param id 执行记录ID
     * @param timestamp 时间戳
     * @param secret 加密字符串
     * @return 执行结果
     */
    Object stop(Integer id, Long timestamp, String secret, Map<String, ?> cancelParams);
}
