package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;
import org.xi.maple.common.model.db.condition.FilterCondition;

/**
 * @author xishihao
 */
public interface PatchByConditionMapper<E, C extends FilterCondition> {

    /**
     * 按条件更新不为空的字段
     *
     * @param entity    更新实体
     * @param condition 更新条件
     * @return 影响的行数
     */
    int patchByCondition(@Param("condition") C condition, @Param("entity") E entity);
}