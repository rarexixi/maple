package org.xi.maple.mp.service.impl;

import org.xi.maple.common.constant.ValidConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    final ClusterMapper clusterMapper;

    @Autowired
    public ClusterServiceImpl(ClusterMapper clusterMapper) {
        this.clusterMapper = clusterMapper;
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
        return getByName(entity.getName());
    }

    /**
     * 批量添加集群
     *
     * @param list 集群列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int batchCreate(List<ClusterSaveReq> list) {
        List<ClusterEntity> entityList = ObjectUtils.copy(list, ClusterEntity.class);
        return clusterMapper.batchInsert(entityList);
    }

    // region 删除/启用/禁用

    /**
     * 删除集群
     *
     * @param nameList 集群名称列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int deleteByName(List<String> nameList, BaseEntity baseEntity) {
        ClusterPkCondition condition = getPkCondition(nameList);
        return clusterMapper.deleteByCondition(condition);
    }

    /**
     * 禁用集群
     *
     * @param nameList 集群名称列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int disableByName(List<String> nameList, BaseEntity baseEntity) {
        ClusterPkCondition condition = getPkCondition(nameList);
        ClusterEntity entity = ObjectUtils.copy(baseEntity, ClusterEntity.class);
        entity.setDisabled(ValidConstant.INVALID);
        return clusterMapper.patchByCondition(condition, entity);
    }

    /**
     * 启用集群
     *
     * @param nameList 集群名称列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int enableByName(List<String> nameList, BaseEntity baseEntity) {
        ClusterPkCondition condition = getPkCondition(nameList);
        ClusterEntity entity = ObjectUtils.copy(baseEntity, ClusterEntity.class);
        entity.setDisabled(ValidConstant.VALID);
        return clusterMapper.patchByCondition(condition, entity);
    }

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新集群非空字段
     *
     * @param name 集群名称
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
        return getByName(name);
    }

    /**
     * 根据更新集群所有字段
     *
     * @param name 集群名称
     * @param saveReq 保存集群请求实体
     * @return 更新后的集群详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ClusterDetailResp updateByName(String name, ClusterSaveReq saveReq) {
        ClusterEntity entity = ObjectUtils.copy(saveReq, ClusterEntity.class);
        clusterMapper.updateByName(name, entity);
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

    /**
     * 分页获取集群列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
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

    private ClusterPkCondition getPkCondition(String name) {
        ClusterPkCondition condition = new ClusterPkCondition();
        condition.setName(name);
        return condition;
    }

    private ClusterPkCondition getPkCondition(List<String> nameList) {
        ClusterPkCondition condition = new ClusterPkCondition();
        if (nameList.isEmpty()) {
            return null;
        } else if (nameList.size() == 1) {
            condition.setName(nameList.get(0));
        } else {
            condition.setNameIn(nameList);
        }
        return condition;
    }
}
