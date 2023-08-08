package org.xi.maple.persistence.mapper;

import org.apache.ibatis.annotations.Param;

public interface InsertMapper<T> {

    /**
     * 添加
     *
     * @param entity 新增实体
     * @return 影响的行数
     */
    int insert(@Param("entity") T entity);
}