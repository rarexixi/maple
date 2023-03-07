package org.xi.maple.datasource.presentation.mapper;

import org.xi.maple.datasource.presentation.condition.DatasourceTypeSelectCondition;
import org.xi.maple.datasource.presentation.condition.DatasourceTypeUpdateCondition;
import org.xi.maple.datasource.presentation.entity.DatasourceTypeEntity;
import org.xi.maple.datasource.presentation.entity.DatasourceTypeEntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 数据源类型数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface DatasourceTypeMapper extends BaseMapper<DatasourceTypeEntity, DatasourceTypeEntityExt, DatasourceTypeUpdateCondition, DatasourceTypeSelectCondition> {

    /**
     * 根据类型编码获取数据源类型详情
     *
     * @param typeCode 类型编码
     * @return 数据源类型详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceTypeEntityExt detail(@Param("typeCode") String typeCode);

    /**
     * 批量添加数据源类型
     *
     * @param list 数据源类型列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchInsert(@Param("list") Collection<DatasourceTypeEntity> list);
}
