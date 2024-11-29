package org.xi.maple.persistence.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.persistence.persistence.condition.EngineExecutionQueueFilterCondition;
import org.xi.maple.persistence.persistence.condition.EngineExecutionQueuePkCondition;
import org.xi.maple.persistence.persistence.entity.EngineExecutionQueueEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 执行队列数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface EngineExecutionQueueMapper extends
        InsertMapper<EngineExecutionQueueEntity>,
        BatchInsertMapper<EngineExecutionQueueEntity>,
        DeleteByConditionMapper<EngineExecutionQueuePkCondition>,
        PatchByConditionMapper<EngineExecutionQueueEntity, EngineExecutionQueuePkCondition>,
        SelectByConditionMapper<EngineExecutionQueueEntity, EngineExecutionQueueFilterCondition>,
        CountByConditionMapper<EngineExecutionQueueFilterCondition> {

    int updateByQueueName(@Param("queueName") String queueName, @Param("entity") EngineExecutionQueueEntity entity);

    EngineExecutionQueueEntity getByQueueName(@Param("queueName") String queueName);
}
