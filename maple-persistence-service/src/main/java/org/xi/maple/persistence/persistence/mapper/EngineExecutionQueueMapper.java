package org.xi.maple.persistence.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.persistence.persistence.condition.EngineExecutionQueueSelectCondition;
import org.xi.maple.persistence.persistence.entity.EngineExecutionQueueEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 执行队列数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface EngineExecutionQueueMapper extends
        InsertMapper<EngineExecutionQueueEntity>,
        SelectByConditionMapper<EngineExecutionQueueEntity, EngineExecutionQueueSelectCondition>,
        SelectByPkMapper<EngineExecutionQueueEntity, String>,
        UpdateByPkMapper<EngineExecutionQueueEntity, String>,
        DeleteByPkMapper<String> {
}
