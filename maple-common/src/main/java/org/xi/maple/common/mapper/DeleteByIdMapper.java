package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author xishihao
 */
public interface DeleteByIdMapper {

    /**
     * 根据ID删除
     *
     * @param id 实体ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Integer id);
}