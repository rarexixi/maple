package org.xi.maple.persistence.persistence.mapper;

import org.xi.maple.common.mapper.*;
import org.xi.maple.persistence.persistence.condition.ApplicationSelectCondition;
import org.xi.maple.persistence.persistence.condition.ApplicationUpdateCondition;
import org.xi.maple.persistence.persistence.entity.ApplicationEntity;
import org.xi.maple.persistence.persistence.entity.ApplicationEntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 访问程序数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface ApplicationMapper extends
        InsertMapper<ApplicationEntity>,
        BatchInsertMapper<ApplicationEntity>,
        DeleteByPkMapper<String>,
        UpdateByPkMapper<ApplicationEntity, String>,
        SelectByPkMapper<ApplicationEntityExt, String>,
        SelectByConditionMapper<ApplicationEntity, ApplicationSelectCondition>,
        CountByConditionMapper<ApplicationSelectCondition> {
}
