package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;
import org.xi.maple.common.model.db.condition.*;

import java.util.List;

/**
 * @author xishihao
 */
public interface SelectByConditionMapper<E, C extends FilterCondition> {

    /**
     * 根据条件查询
     *
     * @param c  过滤条件
     * @param cc 要查询字段
     * @param sc 排序条件
     * @return 实体列表
     */
    List<E> select(@Param("condition") C c, @Param("columnsCondition") ColumnsCondition cc, @Param("sortCondition") SortCondition sc);
}