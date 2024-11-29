package org.xi.maple.mp.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.mp.persistence.condition.DatasourceFilterCondition;
import org.xi.maple.mp.persistence.condition.DatasourcePkCondition;
import org.xi.maple.mp.persistence.entity.DatasourceEntity;
import org.xi.maple.mp.persistence.entity.DatasourceEntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 数据源数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface DatasourceMapper extends
        InsertMapper<DatasourceEntity>,
        BatchInsertMapper<DatasourceEntity>,
        DeleteByConditionMapper<DatasourcePkCondition>,
        PatchByConditionMapper<DatasourceEntity, DatasourcePkCondition>,
        SelectByConditionMapper<DatasourceEntity, DatasourceFilterCondition>,
        CountByConditionMapper<DatasourceFilterCondition> {

    int updateById(@Param("id") Integer id, @Param("entity") DatasourceEntity entity);

    DatasourceEntityExt getById(@Param("id") Integer id);
}
