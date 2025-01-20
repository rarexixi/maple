package org.xi.maple.mp.service.impl;

import org.xi.maple.common.constant.ValidConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.ClusterMessage;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.mp.persistence.condition.ClusterFilterCondition;
import org.xi.maple.mp.persistence.condition.ClusterPkCondition;
import org.xi.maple.mp.persistence.entity.ClusterEntity;
import org.xi.maple.mp.persistence.entity.ClusterEntityExt;
import org.xi.maple.mp.persistence.mapper.ClusterMapper;
import org.xi.maple.mp.model.request.ClusterQueryReq;
import org.xi.maple.mp.model.request.ClusterSaveReq;
import org.xi.maple.mp.model.response.ClusterDetailResp;
import org.xi.maple.mp.model.response.ClusterItemResp;
import org.xi.maple.mp.service.ClusterService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        int count = clusterMapper.insert(entity);
        if (count > 0) sendRefreshClusterMsg(ClusterMessage.Type.ADD, entity.getId());
        return getById(entity.getId());
    }

    // region 删除/启用/禁用

    /**
     * 删除集群
     *
     * @param idList     集群ID列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int deleteById(List<Integer> idList, BaseEntity baseEntity) {
        ClusterPkCondition condition = getPkCondition(idList);
        int count = clusterMapper.deleteByCondition(condition);
        if (count > 0) {
            for (Integer id : idList) {
                sendRefreshClusterMsg(ClusterMessage.Type.DELETE, id);
            }
        }
        return count;
    }

    /**
     * 禁用集群
     *
     * @param idList     集群ID列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int disableById(List<Integer> idList, BaseEntity baseEntity) {
        ClusterPkCondition condition = getPkCondition(idList);
        ClusterEntity entity = ObjectUtils.copy(baseEntity, ClusterEntity.class);
        entity.setDisabled(ValidConstant.INVALID);
        int count = clusterMapper.patchByCondition(condition, entity);
        if (count > 0) {
            for (Integer id : idList) {
                sendRefreshClusterMsg(ClusterMessage.Type.DELETE, id);
            }
        }
        return count;
    }

    /**
     * 启用集群
     *
     * @param idList     集群ID列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int enableById(List<Integer> idList, BaseEntity baseEntity) {
        ClusterPkCondition condition = getPkCondition(idList);
        ClusterEntity entity = ObjectUtils.copy(baseEntity, ClusterEntity.class);
        entity.setDisabled(ValidConstant.VALID);
        int count = clusterMapper.patchByCondition(condition, entity);
        if (count > 0) {
            for (Integer id : idList) {
                sendRefreshClusterMsg(ClusterMessage.Type.ADD, id);
            }
        }
        return count;
    }

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新集群非空字段
     *
     * @param id      集群ID
     * @param saveReq 保存集群请求实体
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterDetailResp patchById(Integer id, ClusterSaveReq saveReq) {
        ClusterPkCondition condition = getPkCondition(id);
        ClusterEntity entity = ObjectUtils.copy(saveReq, ClusterEntity.class);
        int count = clusterMapper.patchByCondition(condition, entity);
        if (count > 0) sendRefreshClusterMsg(ClusterMessage.Type.UPDATE, saveReq.getId());
        return getById(id);
    }

    /**
     * 根据更新集群所有字段
     *
     * @param id      集群ID
     * @param saveReq 保存集群请求实体
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterDetailResp updateById(Integer id, ClusterSaveReq saveReq) {
        ClusterEntity entity = ObjectUtils.copy(saveReq, ClusterEntity.class);
        int count = clusterMapper.updateById(id, entity);
        if (count > 0) sendRefreshClusterMsg(ClusterMessage.Type.UPDATE, saveReq.getId());
        return getById(id);
    }

    // endregion 更新

    // region 详情

    /**
     * 根据获取集群详情
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

    /**
     * 分页获取集群列表
     *
     * @param queryReq 搜索条件
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 符合条件的集群分页列表
     */
    @Override
    public PageList<ClusterItemResp> getPageList(ClusterQueryReq queryReq, Integer pageNum, Integer pageSize) {

        ClusterFilterCondition condition = ObjectUtils.copy(queryReq, ClusterFilterCondition.class);
        ISelect select = () -> clusterMapper.select(condition, null, queryReq.getSort());
        PageInfo<ClusterEntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);

        List<ClusterItemResp> list = ObjectUtils.copy(pageInfo.getList(), ClusterItemResp.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }

    private ClusterPkCondition getPkCondition(Integer id) {
        ClusterPkCondition condition = new ClusterPkCondition();
        condition.setId(id);
        return condition;
    }

    private ClusterPkCondition getPkCondition(List<Integer> idList) {
        ClusterPkCondition condition = new ClusterPkCondition();
        if (idList.isEmpty()) {
            return null;
        } else if (idList.size() == 1) {
            condition.setId(idList.get(0));
        } else {
            condition.setIdIn(idList);
        }
        return condition;
    }

    private void sendRefreshClusterMsg(ClusterMessage.Type type, Integer clusterId) {
        try {
            redisTemplate.convertAndSend(ClusterMessage.CLUSTER_CHANNEL, new ClusterMessage(type, clusterId));
        } catch (Throwable t) {
            logger.error("发送集群刷新消息失败, clusterId: {}, type: {}", clusterId, type, t);
        }
    }
}
