package org.xi.maple.mp.service.impl;

import org.xi.maple.common.constant.ValidConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.mp.persistence.condition.DatasourceFilterCondition;
import org.xi.maple.mp.persistence.condition.DatasourcePkCondition;
import org.xi.maple.mp.persistence.entity.DatasourceEntity;
import org.xi.maple.mp.persistence.entity.DatasourceEntityExt;
import org.xi.maple.mp.persistence.mapper.DatasourceMapper;
import org.xi.maple.mp.model.request.DatasourceQueryReq;
import org.xi.maple.mp.model.request.DatasourceSaveReq;
import org.xi.maple.mp.model.response.DatasourceDetailResp;
import org.xi.maple.mp.model.response.DatasourceItemResp;
import org.xi.maple.mp.service.DatasourceService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据源业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("datasourceService")
public class DatasourceServiceImpl implements DatasourceService {

    final DatasourceMapper datasourceMapper;

    @Autowired
    public DatasourceServiceImpl(DatasourceMapper datasourceMapper) {
        this.datasourceMapper = datasourceMapper;
    }

    /**
     * 添加数据源
     *
     * @param createReq 数据源
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public DatasourceDetailResp create(DatasourceSaveReq createReq) {
        DatasourceEntity entity = ObjectUtils.copy(createReq, DatasourceEntity.class);
        datasourceMapper.insert(entity);
        return getById(entity.getId());
    }

    /**
     * 批量添加数据源
     *
     * @param list 数据源列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int batchCreate(List<DatasourceSaveReq> list) {
        List<DatasourceEntity> entityList = ObjectUtils.copy(list, DatasourceEntity.class);
        return datasourceMapper.batchInsert(entityList);
    }

    // region 删除/启用/禁用

    /**
     * 删除数据源
     *
     * @param idList Id列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int deleteById(List<Integer> idList, BaseEntity baseEntity) {
        DatasourcePkCondition condition = getPkCondition(idList);
        return datasourceMapper.deleteByCondition(condition);
    }

    /**
     * 禁用数据源
     *
     * @param idList Id列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int disableById(List<Integer> idList, BaseEntity baseEntity) {
        DatasourcePkCondition condition = getPkCondition(idList);
        DatasourceEntity entity = ObjectUtils.copy(baseEntity, DatasourceEntity.class);
        entity.setDisabled(ValidConstant.INVALID);
        return datasourceMapper.patchByCondition(condition, entity);
    }

    /**
     * 启用数据源
     *
     * @param idList Id列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int enableById(List<Integer> idList, BaseEntity baseEntity) {
        DatasourcePkCondition condition = getPkCondition(idList);
        DatasourceEntity entity = ObjectUtils.copy(baseEntity, DatasourceEntity.class);
        entity.setDisabled(ValidConstant.VALID);
        return datasourceMapper.patchByCondition(condition, entity);
    }

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新数据源非空字段
     *
     * @param id Id
     * @param saveReq 保存数据源请求实体
     * @return 更新后的数据源详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public DatasourceDetailResp patchById(Integer id, DatasourceSaveReq saveReq) {
        DatasourcePkCondition condition = getPkCondition(id);
        DatasourceEntity entity = ObjectUtils.copy(saveReq, DatasourceEntity.class);
        datasourceMapper.patchByCondition(condition, entity);
        return getById(id);
    }

    /**
     * 根据更新数据源所有字段
     *
     * @param id Id
     * @param saveReq 保存数据源请求实体
     * @return 更新后的数据源详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public DatasourceDetailResp updateById(Integer id, DatasourceSaveReq saveReq) {
        DatasourceEntity entity = ObjectUtils.copy(saveReq, DatasourceEntity.class);
        datasourceMapper.updateById(id, entity);
        return getById(id);
    }

    // endregion 更新

    // region 详情

    /**
     * 根据获取数据源详情
     *
     * @param id Id
     * @return 数据源详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public DatasourceDetailResp getById(Integer id) {
        DatasourceEntityExt entity = datasourceMapper.getById(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("数据源不存在");
        }
        return ObjectUtils.copy(entity, DatasourceDetailResp.class);
    }

    // endregion 详情

    /**
     * 获取数据源列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的数据源列表
     */
    @Override
    public List<DatasourceItemResp> getList(DatasourceQueryReq queryReq) {
        DatasourceFilterCondition condition = ObjectUtils.copy(queryReq, DatasourceFilterCondition.class);
        List<DatasourceEntity> list = datasourceMapper.select(condition, null, queryReq.getSort());
        return ObjectUtils.copy(list, DatasourceItemResp.class);
    }

    /**
     * 分页获取数据源列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的数据源分页列表
     */
    @Override
    public PageList<DatasourceItemResp> getPageList(DatasourceQueryReq queryReq, Integer pageNum, Integer pageSize) {

        DatasourceFilterCondition condition = ObjectUtils.copy(queryReq, DatasourceFilterCondition.class);
        ISelect select = () -> datasourceMapper.select(condition, null, queryReq.getSort());
        PageInfo<DatasourceEntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);

        List<DatasourceItemResp> list = ObjectUtils.copy(pageInfo.getList(), DatasourceItemResp.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }

    private DatasourcePkCondition getPkCondition(Integer id) {
        DatasourcePkCondition condition = new DatasourcePkCondition();
        condition.setId(id);
        return condition;
    }

    private DatasourcePkCondition getPkCondition(List<Integer> idList) {
        DatasourcePkCondition condition = new DatasourcePkCondition();
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
