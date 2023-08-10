package org.xi.maple.persistence.service;

import org.xi.maple.common.model.OperateResult;
import org.xi.maple.persistence.model.request.EngineExecutionQueueSaveRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;

import java.util.List;

/**
 * 执行队列业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface EngineExecutionQueueService {

    /**
     * 添加执行队列
     *
     * @param saveRequest 执行队列
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    OperateResult<Integer> addOrUpdate(EngineExecutionQueueSaveRequest saveRequest);

    /**
     * 删除执行队列
     *
     * @param queueName 执行队列名
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int delete(String queueName);

    /**
     * 根据执行队列名获取执行队列详情
     *
     * @param queueName 执行队列名
     * @return 执行队列详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    EngineExecutionQueue getByQueueName(String queueName);

    /**
     * 获取执行队列列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的执行队列列表
     */
    List<EngineExecutionQueue> getList(EngineExecutionQueueQueryRequest queryRequest);
}
