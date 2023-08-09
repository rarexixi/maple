package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * @author xishihao
 */
public interface BatchInsertMapper<E> {

    /**
     * 添加
     *
     * @param list 新增实体列表
     * @return 影响的行数
     */
    int batchInsert(@Param("list") Collection<E> list);
}