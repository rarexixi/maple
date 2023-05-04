package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;
import org.xi.maple.common.model.UpdateCondition;

/**
 * @author xishihao
 */
public interface UpdateByConditionMapper<E, C extends UpdateCondition> {

    /**
     * 按条件更新
     *
     * @param entity    更新实体
     * @param condition 更新条件
     * @return 影响的行数
     */
    int updateByCondition(@Param("entity") E entity, @Param("condition") C condition);
}