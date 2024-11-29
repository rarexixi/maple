package org.xi.maple.persistence.service.impl;

import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.persistence.condition.ClusterEngineDefaultConfPkCondition;
import org.xi.maple.persistence.persistence.entity.ClusterEngineDefaultConfEntity;
import org.xi.maple.persistence.persistence.mapper.ClusterEngineDefaultConfMapper;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfSaveReq;
import org.xi.maple.persistence.service.ClusterEngineDefaultConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param createReq 集群引擎默认配置
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public Integer create(ClusterEngineDefaultConfSaveReq createReq) {
        ClusterEngineDefaultConfEntity entity = ObjectUtils.copy(createReq, ClusterEngineDefaultConfEntity.class);
        clusterEngineDefaultConfMapper.insert(entity);
        return entity.getId();
    }

    // region 删除/启用/禁用

    /**
     * 删除集群引擎默认配置
     *
     * @param id         引擎ID
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int deleteById(Integer id, BaseEntity baseEntity) {
        ClusterEngineDefaultConfPkCondition condition = getPkCondition(id);
        return clusterEngineDefaultConfMapper.deleteByCondition(condition);
    }

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新集群引擎默认配置
     *
     * @param id      引擎ID
     * @param saveReq 保存集群引擎默认配置请求实体
     * @return 更新后的集群引擎默认配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public Integer patchById(Integer id, ClusterEngineDefaultConfSaveReq saveReq) {
        ClusterEngineDefaultConfPkCondition condition = getPkCondition(id);
        ClusterEngineDefaultConfEntity entity = ObjectUtils.copy(saveReq, ClusterEngineDefaultConfEntity.class);
        return clusterEngineDefaultConfMapper.patchByCondition(condition, entity);
    }

    /**
     * 根据更新集群引擎默认配置
     *
     * @param id      引擎ID
     * @param saveReq 保存集群引擎默认配置请求实体
     * @return 更新后的集群引擎默认配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public Integer updateById(Integer id, ClusterEngineDefaultConfSaveReq saveReq) {
        ClusterEngineDefaultConfEntity entity = ObjectUtils.copy(saveReq, ClusterEngineDefaultConfEntity.class);
        return clusterEngineDefaultConfMapper.updateById(id, entity);
    }

    // endregion 更新

    private ClusterEngineDefaultConfPkCondition getPkCondition(Integer id) {
        ClusterEngineDefaultConfPkCondition condition = new ClusterEngineDefaultConfPkCondition();
        condition.setId(id);
        return condition;
    }
}
