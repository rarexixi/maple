package org.xi.maple.common.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author xishihao
 */
public interface DeleteByPkMapper<PK> {

    /**
     * 根据主键删除
     *
     * @param pk 实体主键
     * @return 影响的行数
     */
    int deleteByPk(@Param("pk") PK pk);
}