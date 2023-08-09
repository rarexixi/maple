package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;
import org.xi.maple.common.model.SelectCondition;

import java.util.List;

/**
 * @author xishihao
 */
public interface CountByConditionMapper<C extends SelectCondition> {

    /**
     * 根据条件查询
     *
     * @param condition 查询条件
     * @return 实体列表
     */
    int count(@Param("condition") C condition);
}