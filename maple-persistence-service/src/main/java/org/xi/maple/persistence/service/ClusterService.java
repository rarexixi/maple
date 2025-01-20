package org.xi.maple.persistence.service;

import org.xi.maple.persistence.model.request.ClusterQueryReq;
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
     * 根据集群ID获取集群详情
     *
     * @param id 集群ID
     * @return 集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ClusterDetailResp getById(Integer id);

    /**
     * 根据集群ID获取集群种类
     *
     * @param id 集群ID
     * @return 集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    String getCategoryById(Integer id);

    /**
     * 获取集群列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的集群列表
     */
    List<ClusterItemResp> getList(ClusterQueryReq queryReq);
}
