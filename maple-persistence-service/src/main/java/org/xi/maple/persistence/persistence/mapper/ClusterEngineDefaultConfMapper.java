package org.xi.maple.persistence.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.persistence.persistence.condition.ClusterEngineDefaultConfFilterCondition;
import org.xi.maple.persistence.persistence.condition.ClusterEngineDefaultConfPkCondition;
import org.xi.maple.persistence.persistence.entity.ClusterEngineDefaultConfEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 集群引擎默认配置数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface ClusterEngineDefaultConfMapper extends
        InsertMapper<ClusterEngineDefaultConfEntity>,
        BatchInsertMapper<ClusterEngineDefaultConfEntity>,
        DeleteByConditionMapper<ClusterEngineDefaultConfPkCondition>,
        PatchByConditionMapper<ClusterEngineDefaultConfEntity, ClusterEngineDefaultConfPkCondition>,
        SelectByConditionMapper<ClusterEngineDefaultConfEntity, ClusterEngineDefaultConfFilterCondition>,
        CountByConditionMapper<ClusterEngineDefaultConfFilterCondition> {

    int updateById(@Param("id") Integer id, @Param("entity") ClusterEngineDefaultConfEntity entity);

    ClusterEngineDefaultConfEntity getById(@Param("id") Integer id);

    ClusterEngineDefaultConfEntity getByTypeAndName(@Param("engineId") Integer engineId, @Param("objType") String objType, @Param("objName") String objName);
}
