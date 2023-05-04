package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author xishihao
 */
public interface UpdateByIdMapper<E> {

    /**
     * 根据ID更新
     *
     * @param entity 更新实体
     * @param id     实体ID
     * @return 影响的行数
     */
    int updateById(@Param("entity") E entity, @Param("id") Integer id);
}