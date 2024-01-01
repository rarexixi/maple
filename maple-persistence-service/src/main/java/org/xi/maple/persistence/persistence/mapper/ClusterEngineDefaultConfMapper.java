package org.xi.maple.persistence.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xi.maple.common.mapper.DeleteByIdMapper;
import org.xi.maple.common.mapper.InsertMapper;
import org.xi.maple.common.mapper.SelectByIdMapper;
import org.xi.maple.common.mapper.UpdateByIdMapper;
import org.xi.maple.persistence.persistence.entity.ClusterEngineDefaultConfEntity;

/**
 * 集群引擎默认配置数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface ClusterEngineDefaultConfMapper extends
        InsertMapper<ClusterEngineDefaultConfEntity>,
        DeleteByIdMapper,
        UpdateByIdMapper<ClusterEngineDefaultConfEntity>,
        SelectByIdMapper<ClusterEngineDefaultConfEntity> {

    ClusterEngineDefaultConfEntity selectByTypeAndName(@Param("engineId") Integer engineId, @Param("objType") String objType, @Param("objName") String objName);
}
