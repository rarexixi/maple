package org.xi.maple.mp.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.mp.persistence.condition.SysConfFilterCondition;
import org.xi.maple.mp.persistence.condition.SysConfPkCondition;
import org.xi.maple.mp.persistence.entity.SysConfEntity;
import org.xi.maple.mp.persistence.entity.SysConfEntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统配置数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface SysConfMapper extends
        InsertMapper<SysConfEntity>,
        BatchInsertMapper<SysConfEntity>,
        DeleteByConditionMapper<SysConfPkCondition>,
        PatchByConditionMapper<SysConfEntity, SysConfPkCondition>,
        SelectByConditionMapper<SysConfEntity, SysConfFilterCondition>,
        CountByConditionMapper<SysConfFilterCondition> {

    int updateByConfKey(@Param("confKey") String confKey, @Param("entity") SysConfEntity entity);

    SysConfEntityExt getByConfKey(@Param("confKey") String confKey);
}
