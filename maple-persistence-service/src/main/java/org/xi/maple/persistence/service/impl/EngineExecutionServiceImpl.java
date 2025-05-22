package org.xi.maple.persistence.service.impl;

import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.exception.MapleDataInsertException;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.persistence.model.request.EngineExecutionPatchReq;
import org.xi.maple.persistence.model.request.EngineExecutionStatusUpdateReq;
import org.xi.maple.persistence.persistence.entity.ClusterEngineEntity;
import org.xi.maple.persistence.persistence.entity.EngineExecutionExtInfoEntity;
import org.xi.maple.persistence.persistence.mapper.ClusterEngineMapper;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.persistence.persistence.condition.EngineExecutionPkCondition;
import org.xi.maple.persistence.persistence.entity.EngineExecutionEntity;
import org.xi.maple.persistence.persistence.entity.EngineExecutionEntityExt;
import org.xi.maple.persistence.persistence.mapper.EngineExecutionMapper;
import org.xi.maple.persistence.model.request.EngineExecutionCreateReq;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;
import org.xi.maple.persistence.service.EngineExecutionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 引擎执行记录业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("engineExecutionService")
public class EngineExecutionServiceImpl implements EngineExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(EngineExecutionServiceImpl.class);

    final EngineExecutionMapper engineExecutionMapper;

    final ClusterEngineMapper clusterEngineMapper;

    @Autowired
    public EngineExecutionServiceImpl(EngineExecutionMapper engineExecutionMapper, ClusterEngineMapper clusterEngineMapper) {
        this.engineExecutionMapper = engineExecutionMapper;
        this.clusterEngineMapper = clusterEngineMapper;
    }

    /**
     * 添加引擎执行记录
     *
     * @param createReq 引擎执行记录
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public Integer create(EngineExecutionCreateReq createReq) {
        EngineExecutionEntity entity = ObjectUtils.copy(createReq, EngineExecutionEntity.class);
        // 设置集群ID
        ClusterEngineEntity engine = clusterEngineMapper.getById(createReq.getEngineId());
        entity.setClusterId(engine.getClusterId());
        entity.setClusterCategory(engine.getClusterCategory());

        int count = engineExecutionMapper.insert(entity);
        if (count > 0) {
            EngineExecutionExtInfoEntity extInfoEntity = new EngineExecutionExtInfoEntity();
            extInfoEntity.setId(entity.getId());
            extInfoEntity.setRunConf(createReq.getRunConf());
            extInfoEntity.setExecConf(createReq.getExecConf());
            extInfoEntity.setExecInfo("{}");
            engineExecutionMapper.insertExt(extInfoEntity);
            return entity.getId();
        }
        throw new MapleDataInsertException("插入失败");
    }

    // region 更新

    /**
     * 根据执行ID更新引擎执行信息
     *
     * @param id      执行ID
     * @param saveReq 保存引擎执行记录请求实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-execution"}, key = "#id")
    @Override
    @Transactional
    public int patchById(Integer id, EngineExecutionPatchReq saveReq) {
        EngineExecutionPkCondition condition = getPkCondition(id);
        EngineExecutionEntity entity = ObjectUtils.copy(saveReq, EngineExecutionEntity.class);
        return engineExecutionMapper.patchByCondition(condition, entity);
    }

    /**
     * 根据执行ID更新引擎执行状态 todo 设置回调
     *
     * @param updateReq 更新引擎执行记录请求实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-execution"}, key = "#id")
    @Transactional
    @Override
    public int updateStatusById(int id, EngineExecutionStatusUpdateReq updateReq) {
        EngineExecutionEntityExt entity = engineExecutionMapper.getById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("引擎执行记录不存在");
        }
        EngineExecutionStatus oldStatus = EngineExecutionStatus.valueOf(entity.getStatus());
        if (oldStatus.isFinalStatus()) {
            logger.warn("引擎执行已结束，id: {}", id);
            return 0;
        }

        EngineExecutionStatus newStatus = EngineExecutionStatus.valueOf(updateReq.getStatus());
        switch (newStatus) {
            case CREATED:
                return 0;
            case ACCEPTED:
                if (!oldStatus.canAccept()) {
                    return 0;
                }
                break;
            case STARTING:
                if (!oldStatus.canStart()) {
                    return 0;
                }
                break;
            case FAILED:
                if (!oldStatus.canStartFailed()) {
                    return engineExecutionMapper.updateStatusById(id, EngineExecutionStatus.START_FAILED.toString());
                }
                break;
            default:
                break;
        }
        // todo 设置 exec-info
        return engineExecutionMapper.updateStatusById(id, updateReq.getStatus());
    }

    /**
     * 根据执行ID更新引擎执行信息
     *
     * @param id        执行ID
     * @param updateReq 引擎执行扩展信息
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-execution"}, key = "#id")
    @Transactional
    @Override
    public int patchExecInfoById(Integer id, Map<String, ?> updateReq) {
        String execInfo;
        if (updateReq == null || updateReq.isEmpty() || (execInfo = JsonUtils.toJsonString(updateReq, null)) == null) {
            return 0;
        }
        return engineExecutionMapper.patchExecInfoById(id, execInfo);
    }

    // endregion 更新

    // region 详情

    /**
     * 根据获取引擎执行记录详情
     *
     * @param id 执行ID
     * @return 引擎执行记录详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Cacheable(cacheNames = {"maple-execution"}, key = "#id")
    @Override
    public EngineExecutionDetailResp getById(Integer id) {
        EngineExecutionEntityExt entity = engineExecutionMapper.getById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("引擎执行记录不存在");
        }
        return ObjectUtils.copy(entity, EngineExecutionDetailResp.class);
    }

    // endregion 详情

    private EngineExecutionPkCondition getPkCondition(Integer id) {
        EngineExecutionPkCondition condition = new EngineExecutionPkCondition();
        condition.setId(id);
        return condition;
    }
}
