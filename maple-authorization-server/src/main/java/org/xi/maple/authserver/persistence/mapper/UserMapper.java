package org.xi.maple.authserver.persistence.mapper;

import org.xi.maple.authserver.persistence.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Mapper
public interface UserMapper {
    UserEntity getByUsername(@Param("username") String username);
    UserEntity getById(@Param("id") Integer id);
    List<String> getUserPermissionsByUserId(@Param("userId") Integer userId);
}
