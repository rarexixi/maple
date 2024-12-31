package org.xi.maple.persistence.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xi.maple.persistence.persistence.entity.JobEntity;

/**
 * 执行作业数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface JobMapper {

    JobEntity getById(@Param("id") Integer id);
}
