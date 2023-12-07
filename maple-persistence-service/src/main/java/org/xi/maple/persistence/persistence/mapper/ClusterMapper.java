package org.xi.maple.persistence.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.persistence.persistence.condition.ClusterSelectCondition;
import org.xi.maple.persistence.persistence.entity.ClusterEntity;
import org.xi.maple.persistence.persistence.entity.ClusterEntityExt;
import org.apache.ibatis.annotations.Mapper;

/**
 * 集群数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface ClusterMapper extends
        InsertMapper<ClusterEntity>,
        DeleteByPkMapper<String>,
        UpdateByPkMapper<ClusterEntity, String>,
        SelectByPkMapper<ClusterEntityExt, String>,
        SelectByConditionMapper<ClusterEntity, ClusterSelectCondition>,
        CountByConditionMapper<ClusterSelectCondition> {
}
