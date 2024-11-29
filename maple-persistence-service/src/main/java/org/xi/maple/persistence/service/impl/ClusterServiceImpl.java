package org.xi.maple.persistence.service.impl;

import org.xi.maple.common.constant.ValidConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.ClusterMessage;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.persistence.condition.ClusterFilterCondition;
import org.xi.maple.persistence.persistence.condition.ClusterPkCondition;
import org.xi.maple.persistence.persistence.entity.ClusterEntity;
import org.xi.maple.persistence.persistence.entity.ClusterEntityExt;
import org.xi.maple.persistence.persistence.mapper.ClusterMapper;
import org.xi.maple.persistence.model.request.ClusterQueryReq;
import org.xi.maple.persistence.model.request.ClusterSaveReq;
import org.xi.maple.persistence.model.response.ClusterDetailResp;
import org.xi.maple.persistence.model.response.ClusterItemResp;
import org.xi.maple.persistence.service.ClusterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    public ClusterServiceImpl(ClusterMapper clusterMapper, RedisTemplate<String, Object> redisTemplate) {
        this.clusterMapper = clusterMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 添加集群
     *
     * @param createReq 集群
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterDetailResp create(ClusterSaveReq createReq) {
        ClusterEntity entity = ObjectUtils.copy(createReq, ClusterEntity.class);
        clusterMapper.insert(entity);
        sendRefreshClusterMsg(ClusterMessage.Type.ADD, createReq.getName());
        return getByName(entity.getName());
    }


    // region 删除/启用/禁用

    /**
     * 删除集群
     *
     * @param name       集群名称
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int deleteByName(String name, BaseEntity baseEntity) {
        ClusterPkCondition condition = getPkCondition(name);
        sendRefreshClusterMsg(ClusterMessage.Type.DELETE, name);
        return clusterMapper.deleteByCondition(condition);
    }

    /**
     * 禁用集群
     *
     * @param name       集群名称
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int disableByName(String name, BaseEntity baseEntity) {
        ClusterPkCondition condition = getPkCondition(name);
        ClusterEntity entity = ObjectUtils.copy(baseEntity, ClusterEntity.class);
        entity.setDisabled(ValidConstant.INVALID);
        sendRefreshClusterMsg(ClusterMessage.Type.DELETE, name);
        return clusterMapper.patchByCondition(condition, entity);
    }

    /**
     * 启用集群
     *
     * @param name       集群名称
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int enableByName(String name, BaseEntity baseEntity) {
        ClusterPkCondition condition = getPkCondition(name);
        ClusterEntity entity = ObjectUtils.copy(baseEntity, ClusterEntity.class);
        entity.setDisabled(ValidConstant.VALID);
        sendRefreshClusterMsg(ClusterMessage.Type.ADD, name);
        return clusterMapper.patchByCondition(condition, entity);
    }

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新集群
     *
     * @param name    集群名称
     * @param saveReq 保存集群请求实体
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterDetailResp patchByName(String name, ClusterSaveReq saveReq) {
        ClusterPkCondition condition = getPkCondition(name);
        ClusterEntity entity = ObjectUtils.copy(saveReq, ClusterEntity.class);
        clusterMapper.patchByCondition(condition, entity);
        sendRefreshClusterMsg(ClusterMessage.Type.UPDATE, saveReq.getName());
        return getByName(name);
    }

    /**
     * 根据更新集群
     *
     * @param name    集群名称
     * @param saveReq 保存集群请求实体
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterDetailResp updateByName(String name, ClusterSaveReq saveReq) {
        ClusterEntity entity = ObjectUtils.copy(saveReq, ClusterEntity.class);
        clusterMapper.updateByName(name, entity);
        sendRefreshClusterMsg(ClusterMessage.Type.UPDATE, name);
        return getByName(name);
    }
    // endregion 更新

    // region 详情

    /**
     * 根据获取集群详情
     *
     * @param name 集群名称
     * @return 集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public ClusterDetailResp getByName(String name) {
        ClusterEntityExt entity = clusterMapper.getByName(name);
        if (entity == null) {
            throw new MapleDataNotFoundException("集群不存在");
        }
        return ObjectUtils.copy(entity, ClusterDetailResp.class);
    }
    // endregion 详情

    /**
     * 获取集群列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的集群列表
     */
    @Override
    public List<ClusterItemResp> getList(ClusterQueryReq queryReq) {
        ClusterFilterCondition condition = ObjectUtils.copy(queryReq, ClusterFilterCondition.class);
        List<ClusterEntity> list = clusterMapper.select(condition, null, queryReq.getSort());
        return ObjectUtils.copy(list, ClusterItemResp.class);
    }

    private void sendRefreshClusterMsg(ClusterMessage.Type type, String clusterName) {
        try {
            redisTemplate.convertAndSend(ClusterMessage.CLUSTER_CHANNEL, new ClusterMessage(type, clusterName));
        } catch (Throwable t) {
            logger.error("发送集群刷新消息失败, cluster: {}, type: {}", clusterName, type, t);
        }
    }

    private ClusterPkCondition getPkCondition(String name) {
        ClusterPkCondition condition = new ClusterPkCondition();
        condition.setName(name);
        return condition;
    }
}
