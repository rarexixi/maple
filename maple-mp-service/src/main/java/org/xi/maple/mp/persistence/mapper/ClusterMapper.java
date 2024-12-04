package org.xi.maple.mp.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.mp.persistence.condition.ClusterFilterCondition;
import org.xi.maple.mp.persistence.condition.ClusterPkCondition;
import org.xi.maple.mp.persistence.entity.ClusterEntity;
import org.xi.maple.mp.persistence.entity.ClusterEntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 集群数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface ClusterMapper extends
        InsertMapper<ClusterEntity>,
        BatchInsertMapper<ClusterEntity>,
        DeleteByConditionMapper<ClusterPkCondition>,
        PatchByConditionMapper<ClusterEntity, ClusterPkCondition>,
        SelectByConditionMapper<ClusterEntity, ClusterFilterCondition>,
        CountByConditionMapper<ClusterFilterCondition> {

    int updateByName(@Param("name") String name, @Param("entity") ClusterEntity entity);

    ClusterEntityExt getByName(@Param("name") String name);
}
