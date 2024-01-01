package org.xi.maple.persistence.service;

import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfAddRequest;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfPatchRequest;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfSaveRequest;

/**
 * 集群引擎默认配置业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface ClusterEngineDefaultConfService {

    /**
     * 添加集群引擎默认配置
     *
     * @param addRequest 集群引擎默认配置
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int add(ClusterEngineDefaultConfAddRequest addRequest);

    /**
     * 删除集群引擎默认配置
     *
     * @param patchRequest 删除条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int delete(ClusterEngineDefaultConfPatchRequest patchRequest);

    /**
     * 根据引擎ID更新集群引擎默认配置
     *
     * @param saveRequest 保存集群引擎默认配置请求实体
     * @return 更新后的集群引擎默认配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int updateById(ClusterEngineDefaultConfSaveRequest saveRequest);
}
