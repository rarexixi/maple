package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author xishihao
 */
public interface InsertMapper<E> {

    /**
     * 添加
     *
     * @param entity 新增实体
     * @return 影响的行数
     */
    int insert(@Param("entity") E entity);
}