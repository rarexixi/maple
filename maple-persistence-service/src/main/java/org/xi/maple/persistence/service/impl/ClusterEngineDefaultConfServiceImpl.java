package org.xi.maple.persistence.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfAddRequest;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfPatchRequest;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfSaveRequest;
import org.xi.maple.persistence.persistence.entity.ClusterEngineDefaultConfEntity;
import org.xi.maple.persistence.persistence.mapper.ClusterEngineDefaultConfMapper;
import org.xi.maple.persistence.service.ClusterEngineDefaultConfService;

/**
 * 集群引擎默认配置业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("clusterEngineDefaultConfService")
public class ClusterEngineDefaultConfServiceImpl implements ClusterEngineDefaultConfService {

    final ClusterEngineDefaultConfMapper clusterEngineDefaultConfMapper;

    @Autowired
    public ClusterEngineDefaultConfServiceImpl(ClusterEngineDefaultConfMapper clusterEngineDefaultConfMapper) {
        this.clusterEngineDefaultConfMapper = clusterEngineDefaultConfMapper;
    }

    /**
     * 添加集群引擎默认配置
     *
     * @param addRequest 集群引擎默认配置
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int add(ClusterEngineDefaultConfAddRequest addRequest) {
        ClusterEngineDefaultConfEntity entity = ObjectUtils.copy(addRequest, ClusterEngineDefaultConfEntity.class);
        clusterEngineDefaultConfMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 删除集群引擎默认配置
     *
     * @param patchRequest 删除条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int delete(ClusterEngineDefaultConfPatchRequest patchRequest) {
        return clusterEngineDefaultConfMapper.deleteById(patchRequest.getId());
    }

    /**
     * 根据引擎ID更新集群引擎默认配置
     *
     * @param saveRequest 保存集群引擎默认配置请求实体
     * @return 更新后的集群引擎默认配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int updateById(ClusterEngineDefaultConfSaveRequest saveRequest) {
        ClusterEngineDefaultConfEntity entity = ObjectUtils.copy(saveRequest, ClusterEngineDefaultConfEntity.class);
        return clusterEngineDefaultConfMapper.updateById(entity, saveRequest.getId());
    }
}
