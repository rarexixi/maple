package org.xi.maple.datasource.persistence.mapper;

import org.xi.maple.datasource.persistence.condition.DatasourceSelectCondition;
import org.xi.maple.datasource.persistence.condition.DatasourceUpdateCondition;
import org.xi.maple.datasource.persistence.entity.DatasourceEntity;
import org.xi.maple.datasource.persistence.entity.DatasourceEntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 数据源配置数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface DatasourceMapper extends BaseMapper<DatasourceEntity, DatasourceEntityExt, DatasourceUpdateCondition, DatasourceSelectCondition> {

    /**
     * 根据Id获取数据源配置详情
     *
     * @param id Id
     * @return 数据源配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceEntityExt detail(@Param("id") Integer id);

    /**
     * 批量添加数据源配置
     *
     * @param list 数据源配置列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchInsert(@Param("list") Collection<DatasourceEntity> list);
}
