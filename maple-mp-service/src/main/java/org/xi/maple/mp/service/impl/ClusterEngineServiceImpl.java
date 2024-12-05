package org.xi.maple.mp.service.impl;

import org.xi.maple.common.constant.ValidConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.mp.persistence.condition.ClusterEngineFilterCondition;
import org.xi.maple.mp.persistence.condition.ClusterEnginePkCondition;
import org.xi.maple.mp.persistence.entity.ClusterEngineEntity;
import org.xi.maple.mp.persistence.entity.ClusterEngineEntityExt;
import org.xi.maple.mp.persistence.mapper.ClusterEngineMapper;
import org.xi.maple.mp.model.request.ClusterEngineQueryReq;
import org.xi.maple.mp.model.request.ClusterEngineSaveReq;
import org.xi.maple.mp.model.response.ClusterEngineDetailResp;
import org.xi.maple.mp.model.response.ClusterEngineItemResp;
import org.xi.maple.mp.service.ClusterEngineService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 计算引擎业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("clusterEngineService")
public class ClusterEngineServiceImpl implements ClusterEngineService {

    final ClusterEngineMapper clusterEngineMapper;

    @Autowired
    public ClusterEngineServiceImpl(ClusterEngineMapper clusterEngineMapper) {
        this.clusterEngineMapper = clusterEngineMapper;
    }

    /**
     * 添加计算引擎
     *
     * @param createReq 计算引擎
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
     * 批量添加计算引擎
     *
     * @param list 计算引擎列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int batchCreate(List<ClusterEngineSaveReq> list) {
        List<ClusterEngineEntity> entityList = ObjectUtils.copy(list, ClusterEngineEntity.class);
        return clusterEngineMapper.batchInsert(entityList);
    }

    // region 删除/启用/禁用

    /**
     * 删除计算引擎
     *
     * @param idList 引擎ID列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int deleteById(List<Integer> idList, BaseEntity baseEntity) {
        ClusterEnginePkCondition condition = getPkCondition(idList);
        return clusterEngineMapper.deleteByCondition(condition);
    }

    /**
     * 禁用计算引擎
     *
     * @param idList 引擎ID列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int disableById(List<Integer> idList, BaseEntity baseEntity) {
        ClusterEnginePkCondition condition = getPkCondition(idList);
        ClusterEngineEntity entity = ObjectUtils.copy(baseEntity, ClusterEngineEntity.class);
        entity.setDisabled(ValidConstant.INVALID);
        return clusterEngineMapper.patchByCondition(condition, entity);
    }

    /**
     * 启用计算引擎
     *
     * @param idList 引擎ID列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int enableById(List<Integer> idList, BaseEntity baseEntity) {
        ClusterEnginePkCondition condition = getPkCondition(idList);
        ClusterEngineEntity entity = ObjectUtils.copy(baseEntity, ClusterEngineEntity.class);
        entity.setDisabled(ValidConstant.VALID);
        return clusterEngineMapper.patchByCondition(condition, entity);
    }

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新计算引擎非空字段
     *
     * @param id 引擎ID
     * @param saveReq 保存计算引擎请求实体
     * @return 更新后的计算引擎详情
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
     * 根据更新计算引擎所有字段
     *
     * @param id 引擎ID
     * @param saveReq 保存计算引擎请求实体
     * @return 更新后的计算引擎详情
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
     * 根据获取计算引擎详情
     *
     * @param id 引擎ID
     * @return 计算引擎详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public ClusterEngineDetailResp getById(Integer id) {
        ClusterEngineEntityExt entity = clusterEngineMapper.getById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("计算引擎不存在");
        }
        return ObjectUtils.copy(entity, ClusterEngineDetailResp.class);
    }

    // endregion 详情

    /**
     * 获取计算引擎列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的计算引擎列表
     */
    @Override
    public List<ClusterEngineItemResp> getList(ClusterEngineQueryReq queryReq) {
        ClusterEngineFilterCondition condition = ObjectUtils.copy(queryReq, ClusterEngineFilterCondition.class);
        List<ClusterEngineEntity> list = clusterEngineMapper.select(condition, null, queryReq.getSort());
        return ObjectUtils.copy(list, ClusterEngineItemResp.class);
    }

    /**
     * 分页获取计算引擎列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的计算引擎分页列表
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

    private ClusterEnginePkCondition getPkCondition(List<Integer> idList) {
        ClusterEnginePkCondition condition = new ClusterEnginePkCondition();
        if (idList.isEmpty()) {
            return null;
        } else if (idList.size() == 1) {
            condition.setId(idList.get(0));
        } else {
            condition.setIdIn(idList);
        }
        return condition;
    }
}
