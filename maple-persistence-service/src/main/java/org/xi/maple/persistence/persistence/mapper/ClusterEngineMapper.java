package org.xi.maple.persistence.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xi.maple.common.mapper.*;
import org.xi.maple.persistence.persistence.condition.ClusterEngineSelectCondition;
import org.xi.maple.persistence.persistence.entity.ClusterEngineEntity;
import org.xi.maple.persistence.persistence.entity.ClusterEngineEntityExt;

/**
 * 集群引擎数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface ClusterEngineMapper extends
        InsertMapper<ClusterEngineEntity>,
        BatchInsertMapper<ClusterEngineEntity>,
        DeleteByIdMapper,
        UpdateByIdMapper<ClusterEngineEntity>,
        SelectByIdMapper<ClusterEngineEntityExt>,
        SelectByConditionMapper<ClusterEngineEntity, ClusterEngineSelectCondition>,
        CountByConditionMapper<ClusterEngineSelectCondition> {
    ClusterEngineEntity detailByClusterEngineVersion(@Param("cluster") String cluster, @Param("name") String name, @Param("version") String version);
}
