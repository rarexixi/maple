package org.xi.maple.persistence.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.OperateResult;
import org.xi.maple.common.util.ObjectUtils;
import org.xi.maple.persistence.persistence.condition.EngineExecutionQueueSelectCondition;
import org.xi.maple.persistence.persistence.entity.EngineExecutionQueueEntity;
import org.xi.maple.persistence.persistence.mapper.EngineExecutionQueueMapper;
import org.xi.maple.persistence.model.request.EngineExecutionQueueSaveRequest;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.persistence.service.EngineExecutionQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 执行队列业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("engineExecutionQueueService")
public class EngineExecutionQueueServiceImpl implements EngineExecutionQueueService {

    final EngineExecutionQueueMapper engineExecutionQueueMapper;

    @Autowired
    public EngineExecutionQueueServiceImpl(EngineExecutionQueueMapper engineExecutionQueueMapper) {
        this.engineExecutionQueueMapper = engineExecutionQueueMapper;
    }

    /**
     * 添加执行队列
     *
     * @param saveRequest 执行队列
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Transactional
    @CacheEvict(cacheNames = {"maple"}, key = "'exec-queue'", condition = "#result.type == T(org.xi.maple.common.constant.OperateResultType).NEW")
    @Override
    public OperateResult<Integer> addOrUpdate(EngineExecutionQueueSaveRequest saveRequest) {
        EngineExecutionQueueEntity entity = ObjectUtils.copy(saveRequest, EngineExecutionQueueEntity.class);
        BeanUtils.copyProperties(saveRequest, entity);
        if (engineExecutionQueueMapper.detailByPk(saveRequest.getQueueName()) == null) {
            return OperateResult.newResult(engineExecutionQueueMapper.insert(entity));
        } else {
            return OperateResult.updateResult(engineExecutionQueueMapper.updateByPk(entity, saveRequest.getQueueName()));
        }
    }

    /**
     * 删除执行队列
     *
     * @param queueName 执行队列名
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Transactional
    @Override
    public int delete(String queueName) {
        return engineExecutionQueueMapper.deleteByPk(queueName);
    }

    /**
     * 根据执行队列名获取执行队列详情
     *
     * @param queueName 执行队列名
     * @return 执行队列详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional(readOnly = true)
    public EngineExecutionQueue getByQueueName(String queueName) {
        EngineExecutionQueueEntity entity = engineExecutionQueueMapper.detailByPk(queueName);
        if (entity == null) {
            throw new MapleDataNotFoundException("执行队列不存在");
        }
        return ObjectUtils.copy(entity, EngineExecutionQueue.class);
    }

    /**
     * 获取执行队列列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的执行队列列表
     */
    @Cacheable(cacheNames = {"maple"}, key = "'exec-queue'")
    @Override
    @Transactional(readOnly = true)
    public List<EngineExecutionQueue> getList(EngineExecutionQueueQueryRequest queryRequest) {
        EngineExecutionQueueSelectCondition condition = ObjectUtils.copy(queryRequest, EngineExecutionQueueSelectCondition.class);
        List<EngineExecutionQueueEntity> list = engineExecutionQueueMapper.select(condition);
        return ObjectUtils.copy(list, EngineExecutionQueue.class);
    }
}
