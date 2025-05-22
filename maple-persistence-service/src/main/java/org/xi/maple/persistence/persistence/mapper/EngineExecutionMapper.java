package org.xi.maple.persistence.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.persistence.persistence.condition.EngineExecutionPkCondition;
import org.xi.maple.persistence.persistence.entity.EngineExecutionEntity;
import org.xi.maple.persistence.persistence.entity.EngineExecutionEntityExt;
import org.xi.maple.persistence.persistence.entity.EngineExecutionExtInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 引擎执行记录数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface EngineExecutionMapper extends
        InsertMapper<EngineExecutionEntity>,
        PatchByConditionMapper<EngineExecutionEntity, EngineExecutionPkCondition> {

    EngineExecutionEntityExt getById(@Param("id") Integer id);

    /**
     * 添加引擎执行记录扩展信息
     *
     * @param entity 新增实体
     * @return 影响的行数
     */
    int insertExt(@Param("entity") EngineExecutionExtInfoEntity entity);

    /**
     * 根据执行ID更新引擎执行状态
     *
     * @param id     引擎执行记录ID
     * @param status 引擎执行状态
     * @return 影响的行数
     */
    int updateStatusById(@Param("id") Integer id, @Param("status") String status);

    /**
     * 根据执行ID更新引擎执行信息
     *
     * @param id       引擎执行记录ID
     * @param execInfo 引擎执行扩展信息
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int patchExecInfoById(@Param("id") Integer id, @Param("execInfo") String execInfo);
}
