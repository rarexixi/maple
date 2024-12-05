package org.xi.maple.mp.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.mp.persistence.condition.JobFilterCondition;
import org.xi.maple.mp.persistence.condition.JobPkCondition;
import org.xi.maple.mp.persistence.entity.JobEntity;
import org.xi.maple.mp.persistence.entity.JobEntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 执行作业数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface JobMapper extends
        InsertMapper<JobEntity>,
        BatchInsertMapper<JobEntity>,
        DeleteByConditionMapper<JobPkCondition>,
        PatchByConditionMapper<JobEntity, JobPkCondition>,
        SelectByConditionMapper<JobEntity, JobFilterCondition>,
        CountByConditionMapper<JobFilterCondition> {

    int updateById(@Param("id") Integer id, @Param("entity") JobEntity entity);

    JobEntityExt getById(@Param("id") Integer id);
}
