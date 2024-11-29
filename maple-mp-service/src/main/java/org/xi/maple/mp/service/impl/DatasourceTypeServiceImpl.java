package org.xi.maple.mp.service.impl;

import org.xi.maple.common.constant.ValidConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.mp.model.DatasourceConfOption;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.mp.persistence.condition.DatasourceTypeFilterCondition;
import org.xi.maple.mp.persistence.condition.DatasourceTypePkCondition;
import org.xi.maple.mp.persistence.entity.DatasourceTypeEntity;
import org.xi.maple.mp.persistence.entity.DatasourceTypeEntityExt;
import org.xi.maple.mp.persistence.mapper.DatasourceTypeMapper;
import org.xi.maple.mp.model.request.DatasourceTypeQueryReq;
import org.xi.maple.mp.model.request.DatasourceTypeSaveReq;
import org.xi.maple.mp.model.response.DatasourceTypeDetailResp;
import org.xi.maple.mp.model.response.DatasourceTypeItemResp;
import org.xi.maple.mp.service.DatasourceTypeService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源类型业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("datasourceTypeService")
public class DatasourceTypeServiceImpl implements DatasourceTypeService {

    final DatasourceTypeMapper datasourceTypeMapper;

    @Autowired
    public DatasourceTypeServiceImpl(DatasourceTypeMapper datasourceTypeMapper) {
        this.datasourceTypeMapper = datasourceTypeMapper;
    }

    /**
     * 添加数据源类型
     *
     * @param createReq 数据源类型
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public DatasourceTypeDetailResp create(DatasourceTypeSaveReq createReq) {
        DatasourceTypeEntity entity = ObjectUtils.copy(createReq, DatasourceTypeEntity.class);
        datasourceTypeMapper.insert(entity);
        return getByTypeCode(entity.getTypeCode());
    }

    /**
     * 批量添加数据源类型
     *
     * @param list 数据源类型列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int batchCreate(List<DatasourceTypeSaveReq> list) {
        List<DatasourceTypeEntity> entityList = ObjectUtils.copy(list, DatasourceTypeEntity.class);
        return datasourceTypeMapper.batchInsert(entityList);
    }

    // region 删除/启用/禁用

    /**
     * 删除数据源类型
     *
     * @param typeCodeList 类型编码列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int deleteByTypeCode(List<String> typeCodeList, BaseEntity baseEntity) {
        DatasourceTypePkCondition condition = getPkCondition(typeCodeList);
        return datasourceTypeMapper.deleteByCondition(condition);
    }

    /**
     * 禁用数据源类型
     *
     * @param typeCodeList 类型编码列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int disableByTypeCode(List<String> typeCodeList, BaseEntity baseEntity) {
        DatasourceTypePkCondition condition = getPkCondition(typeCodeList);
        DatasourceTypeEntity entity = ObjectUtils.copy(baseEntity, DatasourceTypeEntity.class);
        entity.setDisabled(ValidConstant.INVALID);
        return datasourceTypeMapper.patchByCondition(condition, entity);
    }

    /**
     * 启用数据源类型
     *
     * @param typeCodeList 类型编码列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int enableByTypeCode(List<String> typeCodeList, BaseEntity baseEntity) {
        DatasourceTypePkCondition condition = getPkCondition(typeCodeList);
        DatasourceTypeEntity entity = ObjectUtils.copy(baseEntity, DatasourceTypeEntity.class);
        entity.setDisabled(ValidConstant.VALID);
        return datasourceTypeMapper.patchByCondition(condition, entity);
    }

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新数据源类型非空字段
     *
     * @param typeCode 类型编码
     * @param saveReq  保存数据源类型请求实体
     * @return 更新后的数据源类型详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public DatasourceTypeDetailResp patchByTypeCode(String typeCode, DatasourceTypeSaveReq saveReq) {
        DatasourceTypePkCondition condition = getPkCondition(typeCode);
        DatasourceTypeEntity entity = ObjectUtils.copy(saveReq, DatasourceTypeEntity.class);
        datasourceTypeMapper.patchByCondition(condition, entity);
        return getByTypeCode(typeCode);
    }

    /**
     * 根据更新数据源类型所有字段
     *
     * @param typeCode 类型编码
     * @param saveReq  保存数据源类型请求实体
     * @return 更新后的数据源类型详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public DatasourceTypeDetailResp updateByTypeCode(String typeCode, DatasourceTypeSaveReq saveReq) {
        DatasourceTypeEntity entity = ObjectUtils.copy(saveReq, DatasourceTypeEntity.class);
        datasourceTypeMapper.updateByTypeCode(typeCode, entity);
        return getByTypeCode(typeCode);
    }

    // endregion 更新

    // region 详情

    /**
     * 根据获取数据源类型详情
     *
     * @param typeCode 类型编码
     * @return 数据源类型详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public DatasourceTypeDetailResp getByTypeCode(String typeCode) {
        DatasourceTypeEntityExt entity = datasourceTypeMapper.getByTypeCode(typeCode);
        if (entity == null) {
            throw new MapleDataNotFoundException("数据源类型不存在");
        }
        return ObjectUtils.copy(entity, DatasourceTypeDetailResp.class);
    }

    // endregion 详情

    /**
     * 获取数据源类型列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的数据源类型列表
     */
    @Override
    public List<DatasourceTypeItemResp> getList(DatasourceTypeQueryReq queryReq) {
        DatasourceTypeFilterCondition condition = ObjectUtils.copy(queryReq, DatasourceTypeFilterCondition.class);
        List<DatasourceTypeEntity> list = datasourceTypeMapper.select(condition, null, queryReq.getSort());
        return ObjectUtils.copy(list, DatasourceTypeItemResp.class);
    }

    /**
     * 分页获取数据源类型列表
     *
     * @param queryReq 搜索条件
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 符合条件的数据源类型分页列表
     */
    @Override
    public PageList<DatasourceTypeItemResp> getPageList(DatasourceTypeQueryReq queryReq, Integer pageNum, Integer pageSize) {

        DatasourceTypeFilterCondition condition = ObjectUtils.copy(queryReq, DatasourceTypeFilterCondition.class);
        ISelect select = () -> datasourceTypeMapper.select(condition, null, queryReq.getSort());
        PageInfo<DatasourceTypeEntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);

        List<DatasourceTypeItemResp> list = ObjectUtils.copy(pageInfo.getList(), DatasourceTypeItemResp.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }

    private DatasourceTypePkCondition getPkCondition(String typeCode) {
        DatasourceTypePkCondition condition = new DatasourceTypePkCondition();
        condition.setTypeCode(typeCode);
        return condition;
    }

    private DatasourceTypePkCondition getPkCondition(List<String> typeCodeList) {
        DatasourceTypePkCondition condition = new DatasourceTypePkCondition();
        if (typeCodeList.isEmpty()) {
            return null;
        } else if (typeCodeList.size() == 1) {
            condition.setTypeCode(typeCodeList.get(0));
        } else {
            condition.setTypeCodeIn(typeCodeList);
        }
        return condition;
    }
}
