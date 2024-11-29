package org.xi.maple.persistence.service;

import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.model.request.ClusterQueryReq;
import org.xi.maple.persistence.model.request.ClusterSaveReq;
import org.xi.maple.persistence.model.response.ClusterDetailResp;
import org.xi.maple.persistence.model.response.ClusterItemResp;

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
     * @param createReq 集群
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterDetailResp create(ClusterSaveReq createReq);

    /**
     * 删除集群
     *
     * @param name 集群名称
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteByName(String name, BaseEntity entity);

    /**
     * 禁用集群
     *
     * @param name 集群名称
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disableByName(String name, BaseEntity entity);

    /**
     * 启用集群
     *
     * @param name 集群名称
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enableByName(String name, BaseEntity entity);

    /**
     * 根据更新集群
     *
     * @param name 集群名称
     * @param saveReq 保存集群请求实体
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterDetailResp patchByName(String name, ClusterSaveReq saveReq);

    /**
     * 根据更新集群
     *
     * @param name 集群名称
     * @param saveReq 保存集群请求实体
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterDetailResp updateByName(String name, ClusterSaveReq saveReq);

    /**
     * 根据获取集群详情
     *
     * @param name 集群名称
     * @return 集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterDetailResp getByName(String name);

    /**
     * 获取集群列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的集群列表
     */
    List<ClusterItemResp> getList(ClusterQueryReq queryReq);
}
