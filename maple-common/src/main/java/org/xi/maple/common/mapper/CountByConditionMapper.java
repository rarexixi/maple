package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;
import org.xi.maple.common.model.db.condition.FilterCondition;

/**
 * @author xishihao
 */
public interface CountByConditionMapper<C extends FilterCondition> {

    /**
     * 根据条件查询总数
     *
     * @param condition 查询条件
     * @return 总数
     */
    int count(@Param("condition") C condition);
}