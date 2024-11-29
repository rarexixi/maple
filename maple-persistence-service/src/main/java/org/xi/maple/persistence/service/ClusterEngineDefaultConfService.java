package org.xi.maple.persistence.service;

import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfSaveReq;

/**
 * 集群引擎默认配置业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface ClusterEngineDefaultConfService {

    /**
     * 添加集群引擎默认配置
     *
     * @param createReq 集群引擎默认配置
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    Integer create(ClusterEngineDefaultConfSaveReq createReq);

    /**
     * 根据更新集群引擎默认配置
     *
     * @param id      引擎ID
     * @param saveReq 保存集群引擎默认配置请求实体
     * @return 更新后的集群引擎默认配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    Integer patchById(Integer id, ClusterEngineDefaultConfSaveReq saveReq);

    /**
     * 删除集群引擎默认配置
     *
     * @param id     引擎ID列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteById(Integer id, BaseEntity entity);

    /**
     * 根据更新集群引擎默认配置
     *
     * @param id      引擎ID
     * @param saveReq 保存集群引擎默认配置请求实体
     * @return 更新后的集群引擎默认配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    Integer updateById(Integer id, ClusterEngineDefaultConfSaveReq saveReq);
}
