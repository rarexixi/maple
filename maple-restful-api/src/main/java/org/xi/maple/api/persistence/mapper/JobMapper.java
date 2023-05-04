package org.xi.maple.api.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.xi.maple.api.persistence.entity.JobEntity;
import org.xi.maple.common.mapper.InsertMapper;
import org.xi.maple.common.mapper.SelectByIdMapper;
import org.xi.maple.common.mapper.UpdateByIdMapper;

/**
 * @author xishihao
 */
@Mapper
public interface JobMapper extends InsertMapper<JobEntity>, UpdateByIdMapper<JobEntity>, SelectByIdMapper<JobEntity> {
}