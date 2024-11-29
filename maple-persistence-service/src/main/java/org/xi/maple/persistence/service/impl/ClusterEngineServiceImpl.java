package org.xi.maple.persistence.service.impl;

import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.EngineConf;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.persistence.model.request.ClusterEngineDefaultConfGetRequest;
import org.xi.maple.persistence.persistence.entity.ClusterEngineDefaultConfEntity;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.persistence.condition.ClusterEngineFilterCondition;
import org.xi.maple.persistence.persistence.condition.ClusterEnginePkCondition;
import org.xi.maple.persistence.persistence.entity.ClusterEngineEntity;
import org.xi.maple.persistence.persistence.entity.ClusterEngineEntityExt;
import org.xi.maple.persistence.persistence.mapper.ClusterEngineDefaultConfMapper;
import org.xi.maple.persistence.persistence.mapper.ClusterEngineMapper;
import org.xi.maple.persistence.model.request.ClusterEngineQueryReq;
import org.xi.maple.persistence.model.request.ClusterEngineSaveReq;
import org.xi.maple.persistence.model.response.ClusterEngineDetailResp;
import org.xi.maple.persistence.model.response.ClusterEngineItemResp;
import org.xi.maple.persistence.service.ClusterEngineService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 集群引擎业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("clusterEngineService")
public class ClusterEngineServiceImpl implements ClusterEngineService {

    final ClusterEngineMapper clusterEngineMapper;

    final ClusterEngineDefaultConfMapper clusterEngineDefaultConfMapper;

    @Autowired
    public ClusterEngineServiceImpl(ClusterEngineMapper clusterEngineMapper, ClusterEngineDefaultConfMapper clusterEngineDefaultConfMapper) {
        this.clusterEngineMapper = clusterEngineMapper;
        this.clusterEngineDefaultConfMapper = clusterEngineDefaultConfMapper;
    }

    /**
     * 添加集群引擎
     *
     * @param createReq 集群引擎
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterEngineDetailResp create(ClusterEngineSaveReq createReq) {
        ClusterEngineEntity entity = ObjectUtils.copy(createReq, ClusterEngineEntity.class);
        clusterEngineMapper.insert(entity);
        return getById(entity.getId());
    }

    /**
     * 批量添加集群引擎
     *
     * @param list 集群引擎列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int batchCreate(List<ClusterEngineSaveReq> list) {
        List<ClusterEngineEntity> entityList = ObjectUtils.copy(list, ClusterEngineEntity.class);
        return clusterEngineMapper.batchInsert(entityList);
    }

    // region 删除

    /**
     * 删除集群引擎
     *
     * @param id 引擎ID
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int deleteById(Integer id, BaseEntity baseEntity) {
        ClusterEnginePkCondition condition = getPkCondition(id);
        return clusterEngineMapper.deleteByCondition(condition);
    }

    // endregion 删除

    // region 更新

    /**
     * 根据引擎ID更新集群引擎
     *
     * @param id 引擎ID
     * @param saveReq 保存集群引擎请求实体
     * @return 更新后的集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterEngineDetailResp patchById(Integer id, ClusterEngineSaveReq saveReq) {
        ClusterEnginePkCondition condition = getPkCondition(id);
        ClusterEngineEntity entity = ObjectUtils.copy(saveReq, ClusterEngineEntity.class);
        clusterEngineMapper.patchByCondition(condition, entity);
        return getById(id);
    }

    /**
     * 根据引擎ID更新集群引擎
     *
     * @param id 引擎ID
     * @param saveReq 保存集群引擎请求实体
     * @return 更新后的集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterEngineDetailResp updateById(Integer id, ClusterEngineSaveReq saveReq) {
        ClusterEngineEntity entity = ObjectUtils.copy(saveReq, ClusterEngineEntity.class);
        clusterEngineMapper.updateById(id, entity);
        return getById(id);
    }
    // endregion 更新

    // region 详情

    /**
     * 根据引擎ID获取集群引擎详情
     *
     * @param id 引擎ID
     * @return 集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public ClusterEngineDetailResp getById(Integer id) {
        ClusterEngineEntityExt entity = clusterEngineMapper.getById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("集群引擎不存在");
        }
        return ObjectUtils.copy(entity, ClusterEngineDetailResp.class);
    }

    @Override
    public EngineConf getEngineConf(ClusterEngineDefaultConfGetRequest getRequest) {
        ClusterEngineEntity entity = clusterEngineMapper.getByClusterEngineVersion(getRequest.getCluster(), getRequest.getEngine(), getRequest.getVersion());
        if (entity == null) {
            throw new MapleDataNotFoundException("集群引擎不存在");
        }
        EngineConf engineConf = new EngineConf();
        engineConf.setEngineHome(entity.getEngineHome());
        engineConf.setVersion(entity.getVersion());
        engineConf.setEngineExtInfo(JsonUtils.parseObject(entity.getExtInfo(), Map.class, null));

        ClusterEngineDefaultConfEntity groupDefaultConf = clusterEngineDefaultConfMapper.getByTypeAndName(entity.getId(), "group", getRequest.getUserGroup());
        if (groupDefaultConf != null) {
            // todo 合并默认配置
        }
        ClusterEngineDefaultConfEntity userDefaultConf = clusterEngineDefaultConfMapper.getByTypeAndName(entity.getId(), "user", getRequest.getUser());
        if (userDefaultConf != null) {
            // todo 合并默认配置
        }

        return engineConf;
    }
    // endregion 详情

    /**
     * 获取集群引擎列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的集群引擎列表
     */
    @Override
    public List<ClusterEngineItemResp> getList(ClusterEngineQueryReq queryReq) {
        ClusterEngineFilterCondition condition = ObjectUtils.copy(queryReq, ClusterEngineFilterCondition.class);
        List<ClusterEngineEntity> list = clusterEngineMapper.select(condition, null, queryReq.getSort());
        return ObjectUtils.copy(list, ClusterEngineItemResp.class);
    }

    /**
     * 分页获取集群引擎列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的集群引擎分页列表
     */
    @Override
    public PageList<ClusterEngineItemResp> getPageList(ClusterEngineQueryReq queryReq, Integer pageNum, Integer pageSize) {

        ClusterEngineFilterCondition condition = ObjectUtils.copy(queryReq, ClusterEngineFilterCondition.class);
        ISelect select = () -> clusterEngineMapper.select(condition, null, queryReq.getSort());
        PageInfo<ClusterEngineEntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);

        List<ClusterEngineItemResp> list = ObjectUtils.copy(pageInfo.getList(), ClusterEngineItemResp.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }

    private ClusterEnginePkCondition getPkCondition(Integer id) {
        ClusterEnginePkCondition condition = new ClusterEnginePkCondition();
        condition.setId(id);
        return condition;
    }
}
