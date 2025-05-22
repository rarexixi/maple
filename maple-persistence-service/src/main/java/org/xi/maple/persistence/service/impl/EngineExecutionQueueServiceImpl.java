package org.xi.maple.persistence.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.xi.maple.common.model.OperateResult;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.persistence.condition.EngineExecutionQueueFilterCondition;
import org.xi.maple.persistence.persistence.condition.EngineExecutionQueuePkCondition;
import org.xi.maple.persistence.persistence.entity.EngineExecutionQueueEntity;
import org.xi.maple.persistence.persistence.mapper.EngineExecutionQueueMapper;
import org.xi.maple.persistence.model.request.EngineExecutionQueueSaveReq;
import org.xi.maple.persistence.model.response.EngineExecutionQueueResp;
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
    @CacheEvict(cacheNames = {"maple"}, key = "'exec-queue'", condition = "#result.type == T(org.xi.maple.common.constant.OperateResultType).NEW")
    @Override
    @Transactional
    public OperateResult<Integer> upsert(EngineExecutionQueueSaveReq saveRequest) {
        EngineExecutionQueueEntity entity = ObjectUtils.copy(saveRequest, EngineExecutionQueueEntity.class);
        BeanUtils.copyProperties(saveRequest, entity);
        entity.setUpdatedAt(LocalDateTime.now());
        EngineExecutionQueueEntity oldQueue = engineExecutionQueueMapper.getByQueueName(saveRequest.getQueueName());
        if (oldQueue == null) {
            return OperateResult.newResult(engineExecutionQueueMapper.insert(entity));
        } else if (System.currentTimeMillis() - Timestamp.valueOf(oldQueue.getUpdatedAt()).getTime() > 30 * 60 * 1000) {
            // todo 验证时区影响
            return OperateResult.newResult(engineExecutionQueueMapper.updateByQueueName(saveRequest.getQueueName(), entity));
        } else {
            return OperateResult.updateResult(engineExecutionQueueMapper.updateByQueueName(saveRequest.getQueueName(), entity));
        }
    }

    // region 删除

    /**
     * 删除执行队列
     *
     * @param queueName  执行队列名
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple"}, key = "'exec-queue'")
    @Override
    @Transactional
    public int deleteByQueueName(String queueName, BaseEntity baseEntity) {
        EngineExecutionQueuePkCondition condition = getPkCondition(queueName);
        return engineExecutionQueueMapper.deleteByCondition(condition);
    }

    // endregion 删除

    // endregion 详情

    /**
     * 获取执行队列列表
     *
     * @return 符合条件的执行队列列表
     */
    @Cacheable(cacheNames = {"maple"}, key = "'exec-queue'") // todo 考虑如何清理
    @Override
    public List<EngineExecutionQueueResp> getList() {
        EngineExecutionQueueFilterCondition condition = new EngineExecutionQueueFilterCondition();
        condition.setUpdatedAtMin(Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis() - 30 * 60 * 1000)).toLocalDateTime()); // 30分钟内更新过的队列, todo 验证时区影响
        List<EngineExecutionQueueEntity> list = engineExecutionQueueMapper.select(condition, null, null);
        return ObjectUtils.copy(list, EngineExecutionQueueResp.class);
    }

    private EngineExecutionQueuePkCondition getPkCondition(String queueName) {
        EngineExecutionQueuePkCondition condition = new EngineExecutionQueuePkCondition();
        condition.setQueueName(queueName);
        return condition;
    }
}
