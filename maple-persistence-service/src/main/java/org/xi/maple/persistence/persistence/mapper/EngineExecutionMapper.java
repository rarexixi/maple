package org.xi.maple.persistence.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.persistence.persistence.condition.EngineExecutionFilterCondition;
import org.xi.maple.persistence.persistence.condition.EngineExecutionPkCondition;
import org.xi.maple.persistence.persistence.entity.EngineExecutionEntity;
import org.xi.maple.persistence.persistence.entity.EngineExecutionEntityExt;
import org.xi.maple.persistence.persistence.entity.EngineExecutionExtInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 引擎执行记录数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface EngineExecutionMapper extends
        InsertMapper<EngineExecutionEntity>,
        BatchInsertMapper<EngineExecutionEntity>,
        PatchByConditionMapper<EngineExecutionEntity, EngineExecutionPkCondition>,
        SelectByConditionMapper<EngineExecutionEntity, EngineExecutionFilterCondition>,
        CountByConditionMapper<EngineExecutionFilterCondition> {

    EngineExecutionEntityExt getById(@Param("id") Integer id);

    /**
     * 添加引擎执行记录扩展信息
     *
     * @param entity 新增实体
     * @return 影响的行数
     */
    int insertExt(@Param("entity") EngineExecutionExtInfoEntity entity);

    /**
     * 批量添加引擎执行记录扩展信息
     *
     * @param list 新增实体列表
     * @return 影响的行数
     */
    int batchInsertExt(@Param("list") Collection<EngineExecutionExtInfoEntity> list);

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
     * @param id            引擎执行记录ID
     * @param extInfoEntity 引擎执行记录扩展实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int patchExtInfoById(@Param("id") Integer id, @Param("entity") EngineExecutionExtInfoEntity extInfoEntity);
}
