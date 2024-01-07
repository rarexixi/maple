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

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
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
     * 添加执行队列, 如果是新增的队列，或者队列更新时长大于30分钟, 清理缓存
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
        EngineExecutionQueueEntity oldQueue = engineExecutionQueueMapper.detailByPk(saveRequest.getQueueName());
        if (oldQueue == null) {
            return OperateResult.newResult(engineExecutionQueueMapper.insert(entity));
        } else if (System.currentTimeMillis() - Timestamp.valueOf(oldQueue.getUpdateTime()).getTime() > 30 * 60 * 1000) {
            // todo 验证时区影响
            return OperateResult.newResult(engineExecutionQueueMapper.updateByPk(entity, saveRequest.getQueueName()));
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
    @CacheEvict(cacheNames = {"maple"}, key = "'exec-queue'")
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
    @Cacheable(cacheNames = {"maple"}, key = "'exec-queue'") // todo 考虑如何清理
    @Override
    @Transactional(readOnly = true)
    public List<EngineExecutionQueue> getList(EngineExecutionQueueQueryRequest queryRequest) {
        EngineExecutionQueueSelectCondition condition = ObjectUtils.copy(queryRequest, EngineExecutionQueueSelectCondition.class);
        condition.setUpdateTimeMin(Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis() - 30 * 60 * 1000)).toLocalDateTime()); // 30分钟内更新过的队列, todo 验证时区影响
        List<EngineExecutionQueueEntity> list = engineExecutionQueueMapper.select(condition);
        return ObjectUtils.copy(list, EngineExecutionQueue.class);
    }
}
