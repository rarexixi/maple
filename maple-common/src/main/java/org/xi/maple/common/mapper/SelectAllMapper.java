package org.xi.maple.common.mapper;

import java.util.List;

/**
 * @author xishihao
 */
public interface SelectAllMapper<E> {

    /**
     * 获取所有
     *
     * @return 实体列表
     */
    List<E> selectAll();
}