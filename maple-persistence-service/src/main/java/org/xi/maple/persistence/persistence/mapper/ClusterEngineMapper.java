package org.xi.maple.persistence.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.persistence.persistence.condition.ClusterEngineFilterCondition;
import org.xi.maple.persistence.persistence.condition.ClusterEnginePkCondition;
import org.xi.maple.persistence.persistence.entity.ClusterEngineEntity;
import org.xi.maple.persistence.persistence.entity.ClusterEngineEntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 集群引擎数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface ClusterEngineMapper extends
        InsertMapper<ClusterEngineEntity>,
        BatchInsertMapper<ClusterEngineEntity>,
        DeleteByConditionMapper<ClusterEnginePkCondition>,
        PatchByConditionMapper<ClusterEngineEntity, ClusterEnginePkCondition>,
        SelectByConditionMapper<ClusterEngineEntity, ClusterEngineFilterCondition>,
        CountByConditionMapper<ClusterEngineFilterCondition> {

    int updateById(@Param("id") Integer id, @Param("entity") ClusterEngineEntity entity);

    ClusterEngineEntityExt getById(@Param("id") Integer id);

    ClusterEngineEntity getByClusterEngineVersion(@Param("cluster") String cluster, @Param("name") String name, @Param("version") String version);
}
