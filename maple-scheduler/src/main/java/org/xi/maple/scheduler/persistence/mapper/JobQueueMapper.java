package org.xi.maple.scheduler.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.xi.maple.common.mapper.InsertMapper;
import org.xi.maple.common.mapper.SelectAllMapper;
import org.xi.maple.common.mapper.SelectByPkMapper;
import org.xi.maple.common.mapper.UpdateByPkMapper;
import org.xi.maple.scheduler.persistence.entity.JobQueueEntity;

/**
 * @author xishihao
 */
@Mapper
public interface JobQueueMapper extends InsertMapper<JobQueueEntity>, UpdateByPkMapper<JobQueueEntity, String>, SelectByPkMapper<JobQueueEntity, String>, SelectAllMapper<JobQueueEntity> {
}