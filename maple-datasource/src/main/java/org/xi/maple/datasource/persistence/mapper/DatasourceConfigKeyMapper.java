package org.xi.maple.datasource.persistence.mapper;

import org.xi.maple.datasource.persistence.condition.DatasourceConfigKeySelectCondition;
import org.xi.maple.datasource.persistence.condition.DatasourceConfigKeyUpdateCondition;
import org.xi.maple.datasource.persistence.entity.DatasourceConfigKeyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 数据源配置项数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface DatasourceConfigKeyMapper extends BaseMapper<DatasourceConfigKeyEntity, DatasourceConfigKeyEntity, DatasourceConfigKeyUpdateCondition, DatasourceConfigKeySelectCondition> {

    /**
     * 批量添加数据源配置项
     *
     * @param list 数据源配置项列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchInsert(@Param("list") Collection<DatasourceConfigKeyEntity> list);
}
