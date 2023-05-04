package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author xishihao
 */
public interface SelectByPkMapper<E, PK> {

    /**
     * 根据主键获取
     *
     * @param pk 实体主键
     * @return 实体详情
     */
    E detailByPk(@Param("pk") PK pk);
}