package org.xi.maple.persistence.persistence.mapper;

import org.xi.maple.common.mapper.SelectByConditionMapper;
import org.xi.maple.persistence.persistence.condition.ClusterEngineFilterCondition;
import org.xi.maple.persistence.persistence.entity.ClusterEngineEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 集群引擎数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface ClusterEngineMapper extends
        SelectByConditionMapper<ClusterEngineEntity, ClusterEngineFilterCondition> {

    ClusterEngineEntity getById(@Param("id") Integer id);

    String getDefaultConfByTypeAndName(@Param("engineId") Integer engineId, @Param("objType") String objType, @Param("objId") Integer objId);

    ClusterEngineEntity getByClusterEngineVersion(@Param("clusterId") Integer clusterId, @Param("name") String name, @Param("version") String version);
}
