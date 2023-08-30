package org.xi.maple.persistence.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xi.maple.common.constant.DeletedConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.util.ObjectUtils;
import org.xi.maple.persistence.model.request.ClusterAddRequest;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.request.ClusterSaveRequest;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.persistence.persistence.condition.ClusterSelectCondition;
import org.xi.maple.persistence.persistence.entity.ClusterEntity;
import org.xi.maple.persistence.persistence.entity.ClusterEntityExt;
import org.xi.maple.persistence.persistence.mapper.ClusterMapper;
import org.xi.maple.persistence.service.ClusterService;

import java.util.List;

/**
 * 集群业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("clusterService")
public class ClusterServiceImpl implements ClusterService {

    final ClusterMapper clusterMapper;

    @Autowired
    public ClusterServiceImpl(ClusterMapper clusterMapper) {
        this.clusterMapper = clusterMapper;
    }

    /**
     * 添加集群
     *
     * @param addRequest 集群
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Transactional
    @Override
    public ClusterDetailResponse add(ClusterAddRequest addRequest) {
        ClusterEntity entity = ObjectUtils.copy(addRequest, ClusterEntity.class);
        clusterMapper.insert(entity);
        return getByName(entity.getName());
    }

    /**
     * 删除集群
     *
     * @param name 集群名称
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Transactional
    @Override
    public int delete(String name) {
        return clusterMapper.deleteByPk(name);
    }

    /**
     * 禁用集群
     *
     * @param name 集群名称
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Transactional
    @Override
    public int disable(String name) {
        ClusterEntity entity = new ClusterEntity();
        entity.setDeleted(DeletedConstant.INVALID);
        return clusterMapper.updateByPk(entity, name);
    }

    /**
     * 启用集群
     *
     * @param name 集群名称
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Transactional
    @Override
    public int enable(String name) {
        ClusterEntity entity = new ClusterEntity();
        entity.setDeleted(DeletedConstant.VALID);
        return clusterMapper.updateByPk(entity, name);
    }

    /**
     * 根据集群名称更新集群
     *
     * @param saveRequest 保存集群请求实体
     * @param name 集群名称
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Transactional
    @Override
    public ClusterDetailResponse updateByName(ClusterSaveRequest saveRequest, String name) {
        ClusterEntity entity = ObjectUtils.copy(saveRequest, ClusterEntity.class);
        clusterMapper.updateByPk(entity, name);
        ClusterDetailResponse result;
        if (saveRequest.getName() == null) {
            result = getByName(name);
        } else {
            result = getByName(saveRequest.getName());
        }
        return result;
    }

    /**
     * 根据集群名称获取集群详情
     *
     * @param name 集群名称
     * @return 集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional(readOnly = true)
    public ClusterDetailResponse getByName(String name) {
        ClusterEntityExt entity = clusterMapper.detailByPk(name);
        if (entity == null) {
            throw new MapleDataNotFoundException("集群不存在");
        }
        return ObjectUtils.copy(entity, ClusterDetailResponse.class);
    }

    /**
     * 获取集群列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的集群列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClusterListItemResponse> getList(ClusterQueryRequest queryRequest) {
        ClusterSelectCondition condition = ObjectUtils.copy(queryRequest, ClusterSelectCondition.class);
        List<ClusterEntity> list = clusterMapper.select(condition);
        return ObjectUtils.copy(list, ClusterListItemResponse.class);
    }
}
