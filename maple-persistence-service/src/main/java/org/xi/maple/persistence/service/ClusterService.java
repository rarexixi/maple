package org.xi.maple.persistence.service;

import org.xi.maple.persistence.model.request.ClusterAddRequest;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.request.ClusterSaveRequest;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;

import java.util.List;

/**
 * 集群业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface ClusterService {

    /**
     * 添加集群
     *
     * @param addRequest 集群
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterDetailResponse add(ClusterAddRequest addRequest);

    /**
     * 删除集群
     *
     * @param name 集群名称
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int delete(String name);

    /**
     * 禁用集群
     *
     * @param name 集群名称
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disable(String name);

    /**
     * 启用集群
     *
     * @param name 集群名称
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enable(String name);

    /**
     * 根据集群名称更新集群
     *
     * @param saveRequest 保存集群请求实体
     * @param name 集群名称
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterDetailResponse updateByName(ClusterSaveRequest saveRequest, String name);

    /**
     * 根据集群名称获取集群详情
     *
     * @param name 集群名称
     * @return 集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterDetailResponse getByName(String name);

    /**
     * 获取集群列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的集群列表
     */
    List<ClusterListItemResponse> getList(ClusterQueryRequest queryRequest);
}
