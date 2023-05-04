package org.xi.maple.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xi.maple.persistence.entity.JobEntity;

@Mapper
public interface JobMapper extends InsertMapper<JobEntity> {

    /**
     * 根据ID更新作业
     *
     * @param entity 作业实体
     * @param id     作业ID
     * @return 影响的行数
     */
    int update(@Param("entity") JobEntity entity, @Param("id") Integer id);
}