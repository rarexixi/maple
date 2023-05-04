package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author xishihao
 */
public interface UpdateByPkMapper<E, PK> {

    /**
     * 根据主键更新
     *
     * @param entity 更新实体
     * @param pk     实体主键
     * @return 影响的行数
     */
    int updateByPk(@Param("entity") E entity, @Param("pk") PK pk);
}