package org.xi.maple.persistence.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.EngineConf;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.common.util.ObjectUtils;
import org.xi.maple.persistence.model.request.*;
import org.xi.maple.persistence.model.response.ClusterEngineDetailResponse;
import org.xi.maple.persistence.model.response.ClusterEngineListItemResponse;
import org.xi.maple.persistence.persistence.condition.ClusterEngineSelectCondition;
import org.xi.maple.persistence.persistence.entity.ClusterEngineDefaultConfEntity;
import org.xi.maple.persistence.persistence.entity.ClusterEngineEntity;
import org.xi.maple.persistence.persistence.entity.ClusterEngineEntityExt;
import org.xi.maple.persistence.persistence.mapper.ClusterEngineDefaultConfMapper;
import org.xi.maple.persistence.persistence.mapper.ClusterEngineMapper;
import org.xi.maple.persistence.service.ClusterEngineService;

import java.util.Collection;
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
     * @param addRequest 集群引擎
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterEngineDetailResponse add(ClusterEngineAddRequest addRequest) {
        ClusterEngineEntity entity = ObjectUtils.copy(addRequest, ClusterEngineEntity.class);
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
    public int batchAdd(Collection<ClusterEngineAddRequest> list) {
        List<ClusterEngineEntity> entityList = ObjectUtils.copy(list, ClusterEngineEntity.class);
        return clusterEngineMapper.batchInsert(entityList);
    }

    /**
     * 删除集群引擎
     *
     * @param patchRequest 删除条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int delete(ClusterEnginePatchRequest patchRequest) {
        return clusterEngineMapper.deleteById(patchRequest.getId());
    }

    /**
     * 根据引擎ID更新集群引擎
     *
     * @param saveRequest 保存集群引擎请求实体
     * @return 更新后的集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterEngineDetailResponse updateById(ClusterEngineSaveRequest saveRequest) {
        ClusterEngineEntity entity = ObjectUtils.copy(saveRequest, ClusterEngineEntity.class);
        clusterEngineMapper.updateById(entity, saveRequest.getId());
        return getById(saveRequest.getId());
    }

    /**
     * 根据引擎ID获取集群引擎详情
     *
     * @param id 引擎ID
     * @return 集群引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public ClusterEngineDetailResponse getById(Integer id) {
        ClusterEngineEntityExt entity = clusterEngineMapper.detailById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("集群引擎不存在");
        }
        return ObjectUtils.copy(entity, ClusterEngineDetailResponse.class);
    }

    @Override
    public EngineConf getEngineConf(ClusterEngineDefaultConfGetRequest getRequest) {
        ClusterEngineEntity entity = clusterEngineMapper.detailByClusterEngineVersion(getRequest.getCluster(), getRequest.getEngine(), getRequest.getVersion());
        if (entity == null) {
            throw new MapleDataNotFoundException("集群引擎不存在");
        }
        EngineConf engineConf = new EngineConf();
        engineConf.setEngineHome(entity.getEngineHome());
        engineConf.setVersion(entity.getVersion());
        engineConf.setEngineExtInfo(JsonUtils.parseObject(entity.getExtInfo(), Map.class, null));

        ClusterEngineDefaultConfEntity groupDefaultConf = clusterEngineDefaultConfMapper.selectByTypeAndName(entity.getId(), "group", getRequest.getUserGroup());
        if (groupDefaultConf != null) {
            // todo 合并默认配置
        }
        ClusterEngineDefaultConfEntity userDefaultConf = clusterEngineDefaultConfMapper.selectByTypeAndName(entity.getId(), "user", getRequest.getUser());
        if (userDefaultConf != null) {
            // todo 合并默认配置
        }

        return engineConf;
    }

    /**
     * 获取集群引擎列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的集群引擎列表
     */
    @Override
    public List<ClusterEngineListItemResponse> getList(ClusterEngineQueryRequest queryRequest) {
        ClusterEngineSelectCondition condition = ObjectUtils.copy(queryRequest, ClusterEngineSelectCondition.class);
        List<ClusterEngineEntity> list = clusterEngineMapper.select(condition);
        return ObjectUtils.copy(list, ClusterEngineListItemResponse.class);
    }

    /**
     * 分页获取集群引擎列表
     *
     * @param queryRequest 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的集群引擎分页列表
     */
    @Override
    public PageList<ClusterEngineListItemResponse> getPageList(ClusterEngineQueryRequest queryRequest, Integer pageNum, Integer pageSize) {

        ClusterEngineSelectCondition condition = ObjectUtils.copy(queryRequest, ClusterEngineSelectCondition.class);
        PageInfo<ClusterEngineEntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> clusterEngineMapper.select(condition));

        List<ClusterEngineListItemResponse> list = ObjectUtils.copy(pageInfo.getList(), ClusterEngineListItemResponse.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }
}
