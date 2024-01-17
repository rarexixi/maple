package org.xi.maple.persistence.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xi.maple.common.constant.DeletedConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.ClusterMessage;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.persistence.model.request.ClusterAddRequest;
import org.xi.maple.persistence.model.request.ClusterPatchRequest;
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

    private static final Logger logger = LoggerFactory.getLogger(ClusterServiceImpl.class);

    final ClusterMapper clusterMapper;
    final RedisTemplate<String, Object> redisTemplate;

    public ClusterServiceImpl(ClusterMapper clusterMapper, RedisTemplate<String, Object> redisTemplate) {
        this.clusterMapper = clusterMapper;
        this.redisTemplate = redisTemplate;
    }


    /**
     * 添加集群
     *
     * @param addRequest 集群
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterDetailResponse add(ClusterAddRequest addRequest) {
        ClusterEntity entity = ObjectUtils.copy(addRequest, ClusterEntity.class);
        clusterMapper.insert(entity);
        sendRefreshClusterMsg(ClusterMessage.Type.ADD, addRequest.getName());
        return getByName(entity.getName());
    }

    /**
     * 删除集群
     *
     * @param patchRequest 删除条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int delete(ClusterPatchRequest patchRequest) {
        int count = clusterMapper.deleteByPk(patchRequest.getName());
        sendRefreshClusterMsg(ClusterMessage.Type.DELETE, patchRequest.getName());
        return count;
    }

    /**
     * 禁用集群
     *
     * @param patchRequest 禁用条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int disable(ClusterPatchRequest patchRequest) {
        ClusterEntity entity = ObjectUtils.copy(patchRequest, ClusterEntity.class, "name");
        entity.setDeleted(DeletedConstant.INVALID);
        int count = clusterMapper.updateByPk(entity, patchRequest.getName());
        sendRefreshClusterMsg(ClusterMessage.Type.DELETE, patchRequest.getName());
        return count;
    }

    /**
     * 启用集群
     *
     * @param patchRequest 启用条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int enable(ClusterPatchRequest patchRequest) {
        ClusterEntity entity = ObjectUtils.copy(patchRequest, ClusterEntity.class, "name");
        entity.setDeleted(DeletedConstant.VALID);
        int count = clusterMapper.updateByPk(entity, patchRequest.getName());
        sendRefreshClusterMsg(ClusterMessage.Type.ADD, patchRequest.getName());
        return count;
    }

    /**
     * 根据集群名称更新集群
     *
     * @param saveRequest 保存集群请求实体
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterDetailResponse updateByName(ClusterSaveRequest saveRequest) {
        ClusterEntity entity = ObjectUtils.copy(saveRequest, ClusterEntity.class);
        clusterMapper.updateByPk(entity, saveRequest.getName());
        sendRefreshClusterMsg(ClusterMessage.Type.UPDATE, saveRequest.getName());
        return getByName(saveRequest.getName());
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

    private void sendRefreshClusterMsg(ClusterMessage.Type type, String clusterName) {
        try {
            redisTemplate.convertAndSend(ClusterMessage.CLUSTER_CHANNEL, new ClusterMessage(type, clusterName));
        } catch (Throwable t) {
            logger.error("发送集群刷新消息失败, cluster: {}, type: {}", clusterName, type, t);
        }
    }
}
