package org.xi.maple.datacalc.api.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.xi.maple.datacalc.api.persistence.entity.JobQueueEntity;
import org.xi.maple.common.mapper.*;

/**
 * @author xishihao
 */
@Mapper
public interface JobQueueMapper extends InsertMapper<JobQueueEntity>, UpdateByPkMapper<JobQueueEntity, String>, SelectByPkMapper<JobQueueEntity, String> {
}