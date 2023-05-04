package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author xishihao
 */
public interface SelectByIdMapper<E> {

    /**
     * 根据ID获取
     *
     * @param id 实体ID
     * @return 实体详情
     */
    E detailById(@Param("id") Integer id);
}