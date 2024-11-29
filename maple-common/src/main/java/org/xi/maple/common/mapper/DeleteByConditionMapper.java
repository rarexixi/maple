package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;
import org.xi.maple.common.model.db.condition.FilterCondition;

/**
 * @author xishihao
 */
public interface DeleteByConditionMapper<C extends FilterCondition> {

    /**
     * 根据ID删除
     *
     * @param condition 删除条件
     * @return 影响的行数
     */
    int deleteByCondition(@Param("condition") C condition);
}