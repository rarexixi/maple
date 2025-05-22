package org.xi.maple.persistence.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.persistence.persistence.condition.ClusterFilterCondition;
import org.xi.maple.persistence.persistence.entity.ClusterEntity;
import org.xi.maple.persistence.persistence.entity.ClusterEntityExt;
import org.xi.maple.persistence.persistence.mapper.ClusterMapper;
import org.xi.maple.persistence.model.request.ClusterQueryReq;
import org.xi.maple.persistence.model.response.ClusterDetailResp;
import org.xi.maple.persistence.model.response.ClusterItemResp;
import org.xi.maple.persistence.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 集群业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("clusterService")
public class ClusterServiceImpl implements ClusterService {

    final ClusterMapper clusterMapper;
    final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public ClusterServiceImpl(ClusterMapper clusterMapper, RedisTemplate<String, Object> redisTemplate) {
        this.clusterMapper = clusterMapper;
        this.redisTemplate = redisTemplate;
    }

    // region 详情

    /**
     * 根据集群ID获取集群详情
     *
     * @param id 集群ID
     * @return 集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public ClusterDetailResp getById(Integer id) {
        ClusterEntityExt entity = clusterMapper.getById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("集群不存在");
        }
        return ObjectUtils.copy(entity, ClusterDetailResp.class);
    }

    /**
     * 根据集群ID获取集群种类
     *
     * @param id 集群ID
     * @return 集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Cacheable(value = "cluster::category", key = "#id")
    @Override
    public String getCategoryById(Integer id) {
        return clusterMapper.getCategoryById(id);
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
        List<ClusterEntity> list = clusterMapper.select(condition, null, null);
        return ObjectUtils.copy(list, ClusterItemResp.class);
    }
}
