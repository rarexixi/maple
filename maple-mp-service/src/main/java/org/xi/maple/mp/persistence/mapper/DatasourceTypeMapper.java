package org.xi.maple.mp.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.mp.persistence.condition.DatasourceTypeFilterCondition;
import org.xi.maple.mp.persistence.condition.DatasourceTypePkCondition;
import org.xi.maple.mp.persistence.entity.DatasourceTypeEntity;
import org.xi.maple.mp.persistence.entity.DatasourceTypeEntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 数据源类型数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface DatasourceTypeMapper extends
        InsertMapper<DatasourceTypeEntity>,
        BatchInsertMapper<DatasourceTypeEntity>,
        DeleteByConditionMapper<DatasourceTypePkCondition>,
        PatchByConditionMapper<DatasourceTypeEntity, DatasourceTypePkCondition>,
        SelectByConditionMapper<DatasourceTypeEntity, DatasourceTypeFilterCondition>,
        CountByConditionMapper<DatasourceTypeFilterCondition> {

    int updateByTypeCode(@Param("typeCode") String typeCode, @Param("entity") DatasourceTypeEntity entity);

    DatasourceTypeEntityExt getByTypeCode(@Param("typeCode") String typeCode);
}
