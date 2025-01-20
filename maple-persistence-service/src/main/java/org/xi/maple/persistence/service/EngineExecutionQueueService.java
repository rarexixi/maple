package org.xi.maple.persistence.service;

import org.xi.maple.common.model.OperateResult;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryReq;
import org.xi.maple.persistence.model.request.EngineExecutionQueueSaveReq;
import org.xi.maple.persistence.model.response.EngineExecutionQueueResp;

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
    OperateResult<Integer> upsert(EngineExecutionQueueSaveReq saveRequest);

    /**
     * 删除执行队列
     *
     * @param queueName  执行队列名
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteByQueueName(String queueName, BaseEntity entity);

    /**
     * 获取执行队列列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的执行队列列表
     */
    List<EngineExecutionQueueResp> getList(EngineExecutionQueueQueryReq queryReq);
}
