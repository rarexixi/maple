package org.xi.maple.mp.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.mp.persistence.condition.ApplicationFilterCondition;
import org.xi.maple.mp.persistence.condition.ApplicationPkCondition;
import org.xi.maple.mp.persistence.entity.ApplicationEntity;
import org.xi.maple.mp.persistence.entity.ApplicationEntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 访问程序数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface ApplicationMapper extends
        InsertMapper<ApplicationEntity>,
        BatchInsertMapper<ApplicationEntity>,
        DeleteByConditionMapper<ApplicationPkCondition>,
        PatchByConditionMapper<ApplicationEntity, ApplicationPkCondition>,
        SelectByConditionMapper<ApplicationEntity, ApplicationFilterCondition>,
        CountByConditionMapper<ApplicationFilterCondition> {

    int updateByAppName(@Param("appName") String appName, @Param("entity") ApplicationEntity entity);

    ApplicationEntityExt getByAppName(@Param("appName") String appName);
}
