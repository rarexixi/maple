package org.xi.maple.mp.service;

import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.ClusterQueryReq;
import org.xi.maple.mp.model.request.ClusterSaveReq;
import org.xi.maple.mp.model.response.ClusterDetailResp;
import org.xi.maple.mp.model.response.ClusterItemResp;

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
     * 批量添加集群
     *
     * @param list 集群列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchCreate(List<ClusterSaveReq> list);

    // region 删除/启用/禁用

    /**
     * 删除集群
     *
     * @param nameList 集群名称列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteByName(List<String> nameList, BaseEntity entity);

    /**
     * 禁用集群
     *
     * @param nameList 集群名称列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disableByName(List<String> nameList, BaseEntity entity);

    /**
     * 启用集群
     *
     * @param nameList 集群名称列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enableByName(List<String> nameList, BaseEntity entity);

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新集群非空字段
     *
     * @param name 集群名称
     * @param saveReq 保存集群请求实体
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterDetailResp patchByName(String name, ClusterSaveReq saveReq);

    /**
     * 根据更新集群所有字段
     *
     * @param name 集群名称
     * @param saveReq 保存集群请求实体
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterDetailResp updateByName(String name, ClusterSaveReq saveReq);

    // endregion 更新

    // region 详情

    /**
     * 根据获取集群详情
     *
     * @param name 集群名称
     * @return 集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterDetailResp getByName(String name);

    // endregion 详情

    /**
     * 获取集群列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的集群列表
     */
    List<ClusterItemResp> getList(ClusterQueryReq queryReq);

    /**
     * 分页获取集群列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的集群分页列表
     */
    PageList<ClusterItemResp> getPageList(ClusterQueryReq queryReq, Integer pageNum, Integer pageSize);
}
