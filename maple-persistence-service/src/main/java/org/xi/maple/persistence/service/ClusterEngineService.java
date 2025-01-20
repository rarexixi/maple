package org.xi.maple.persistence.service;

import org.xi.maple.common.model.EngineConf;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfGetRequest;

/**
 * 集群引擎业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface ClusterEngineService {

    /**
     * 根据 引擎ID和用户、用户组 获取集群引擎配置信息
     *
     * @param id         引擎ID
     * @param getRequest 查询请求
     * @return 集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    EngineConf getEngineConf(Integer id, ClusterEngineDefaultConfGetRequest getRequest);
}
