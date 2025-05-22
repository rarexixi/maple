package org.xi.maple.persistence.persistence.mapper;

import org.xi.maple.persistence.persistence.entity.ApplicationEntityExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 访问程序数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface ApplicationMapper {

    ApplicationEntityExt getByAppName(@Param("appName") String appName);
}
