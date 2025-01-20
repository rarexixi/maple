package org.xi.maple.manager.service;

import org.xi.maple.persistence.model.response.ClusterDetailResp;
import org.xi.maple.manager.model.ClusterQueue;

public interface ClusterService {

    ClusterQueue getCachedQueueInfo(Integer clusterId, String queue);

    /**
     * 删除集群配置
     *
     * @param clusterId 集群ID
     */
    void removeClusterConfig(Integer clusterId);

    /**
     * 新增集群配置
     *
     * @param cluster 集群详情
     */
    void addClusterConfig(ClusterDetailResp cluster);

    /**
     * 强制刷新所有集群配置
     */
    void refreshAllClusterConfig();

    /**
     * 开启刷新集群配置（仅配置内容）
     */
    void startRefreshScheduler();

    /**
     * 关闭刷新集群配置（仅配置内容）
     */
    void stopRefreshScheduler();
}
