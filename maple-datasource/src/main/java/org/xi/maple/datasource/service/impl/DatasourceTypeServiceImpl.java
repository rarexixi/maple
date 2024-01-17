package org.xi.maple.datasource.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.datasource.constant.DeletedConstant;
import org.xi.maple.datasource.model.request.*;
import org.xi.maple.datasource.model.response.DatasourceConfigKeyListItemResponse;
import org.xi.maple.datasource.persistence.condition.DatasourceConfigKeySelectCondition;
import org.xi.maple.datasource.persistence.condition.DatasourceTypeSelectCondition;
import org.xi.maple.datasource.persistence.condition.DatasourceTypeUpdateCondition;
import org.xi.maple.datasource.persistence.entity.DatasourceConfigKeyEntity;
import org.xi.maple.datasource.persistence.entity.DatasourceTypeEntity;
import org.xi.maple.datasource.persistence.entity.DatasourceTypeEntityExt;
import org.xi.maple.datasource.persistence.mapper.DatasourceConfigKeyMapper;
import org.xi.maple.datasource.persistence.mapper.DatasourceTypeMapper;
import org.xi.maple.datasource.model.response.DatasourceTypeDetailResponse;
import org.xi.maple.datasource.model.response.DatasourceTypeListItemResponse;
import org.xi.maple.datasource.service.DatasourceTypeService;
import org.xi.maple.service.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据源类型业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("datasourceTypeService")
@Transactional
public class DatasourceTypeServiceImpl implements DatasourceTypeService {

    final DatasourceTypeMapper datasourceTypeMapper;
    final DatasourceConfigKeyMapper datasourceConfigKeyMapper;

    @Autowired
    public DatasourceTypeServiceImpl(DatasourceTypeMapper datasourceTypeMapper, DatasourceConfigKeyMapper datasourceConfigKeyMapper) {
        this.datasourceTypeMapper = datasourceTypeMapper;
        this.datasourceConfigKeyMapper = datasourceConfigKeyMapper;
    }

    /**
     * 添加数据源类型
     *
     * @param addRequest 数据源类型
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public DatasourceTypeDetailResponse add(DatasourceTypeAddRequest addRequest) {
        DatasourceTypeEntity entity = ObjectUtils.copy(addRequest, DatasourceTypeEntity.class);
        datasourceTypeMapper.insert(entity);
        saveConfigKeys(entity.getTypeCode(), addRequest.getConfigKeys());
        return getByTypeCode(entity.getTypeCode());
    }

    /**
     * 删除数据源类型
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public int delete(DatasourceTypePatchRequest patchRequest) {
        DatasourceTypeSelectCondition condition = ObjectUtils.copy(patchRequest, DatasourceTypeSelectCondition.class);
        deleteConfigKeys(condition);
        return datasourceTypeMapper.delete(condition);
    }

    /**
     * 禁用数据源类型
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public int disable(DatasourceTypePatchRequest patchRequest) {
        DatasourceTypeUpdateCondition condition = ObjectUtils.copy(patchRequest, DatasourceTypeUpdateCondition.class);
        DatasourceTypeEntity entity = ObjectUtils.copy(patchRequest, DatasourceTypeEntity.class, "typeCode");
        entity.setDeleted(DeletedConstant.INVALID);
        return datasourceTypeMapper.update(entity, condition);
    }

    /**
     * 启用数据源类型
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public int enable(DatasourceTypePatchRequest patchRequest) {
        DatasourceTypeUpdateCondition condition = ObjectUtils.copy(patchRequest, DatasourceTypeUpdateCondition.class);
        DatasourceTypeEntity entity = ObjectUtils.copy(patchRequest, DatasourceTypeEntity.class, "typeCode");
        entity.setDeleted(DeletedConstant.VALID);
        return datasourceTypeMapper.update(entity, condition);
    }

    /**
     * 根据类型编码更新数据源类型
     *
     * @param saveRequest 保存数据源类型请求实体
     * @param typeCode    类型编码
     * @return 更新后的数据源类型详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public DatasourceTypeDetailResponse updateByTypeCode(DatasourceTypeSaveRequest saveRequest, String typeCode) {
        DatasourceTypeUpdateCondition condition = new DatasourceTypeUpdateCondition();
        condition.setTypeCode(typeCode);
        DatasourceTypeEntity entity = ObjectUtils.copy(saveRequest, DatasourceTypeEntity.class);
        datasourceTypeMapper.update(entity, condition);
        deleteConfigKeys(typeCode);
        DatasourceTypeDetailResponse result;
        if (StringUtils.isBlank(saveRequest.getTypeCode())) {
            saveConfigKeys(typeCode, saveRequest.getConfigKeys());
            result = getByTypeCode(typeCode);
        } else {
            saveConfigKeys(entity.getTypeCode(), saveRequest.getConfigKeys());
            result = getByTypeCode(saveRequest.getTypeCode());
        }
        return result;
    }

    /**
     * 根据类型编码获取数据源类型详情
     *
     * @param typeCode 类型编码
     * @return 数据源类型详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional(readOnly = true)
    public DatasourceTypeDetailResponse getByTypeCode(String typeCode) {
        DatasourceTypeEntityExt entity = datasourceTypeMapper.detail(typeCode);
        if (entity == null) {
            throw new MapleDataNotFoundException("数据源类型不存在");
        }
        DatasourceConfigKeySelectCondition condition = new DatasourceConfigKeySelectCondition();
        condition.setDatasourceType(typeCode);
        DatasourceTypeDetailResponse detail = ObjectUtils.copy(entity, DatasourceTypeDetailResponse.class);
        detail.setConfigKeys(getConfigKeys(typeCode));
        return detail;
    }

    private List<DatasourceConfigKeyListItemResponse> getConfigKeys(String typeCode) {
        DatasourceConfigKeySelectCondition condition = new DatasourceConfigKeySelectCondition();
        condition.setDatasourceType(typeCode);
        condition.orderBy("key_order", SortConstants.ASC);
        List<DatasourceConfigKeyEntity> list = datasourceConfigKeyMapper.select(condition);
        return ObjectUtils.copy(list, DatasourceConfigKeyListItemResponse.class);
    }


    /**
     * 获取数据源类型列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的数据源类型列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<DatasourceTypeListItemResponse> getList(DatasourceTypeQueryRequest queryRequest) {
        DatasourceTypeSelectCondition condition = ObjectUtils.copy(queryRequest, DatasourceTypeSelectCondition.class);
        List<DatasourceTypeEntityExt> list = datasourceTypeMapper.select(condition);
        return ObjectUtils.copy(list, DatasourceTypeListItemResponse.class);
    }

    private int saveConfigKeys(String datasourceType, Collection<DatasourceConfigKeySaveRequest> list) {
        AtomicInteger i = new AtomicInteger(0);
        List<DatasourceConfigKeyEntity> entityList = ObjectUtils.copy(list, DatasourceConfigKeyEntity.class, item -> {
            item.setDatasourceType(datasourceType);
            item.setKeyOrder(i.addAndGet(1));
        });

        int result = 0;
        for (DatasourceConfigKeyEntity entity : entityList) {
            result += datasourceConfigKeyMapper.insert(entity);
        }
        return result;
    }

    private int deleteConfigKeys(String datasourceType) {
        DatasourceConfigKeySelectCondition condition = new DatasourceConfigKeySelectCondition();
        condition.setDatasourceType(datasourceType);
        return datasourceConfigKeyMapper.delete(condition);
    }

    private int deleteConfigKeys(DatasourceTypeSelectCondition deleteCondition) {
        DatasourceConfigKeySelectCondition condition = new DatasourceConfigKeySelectCondition();
        condition.setDatasourceType(deleteCondition.getTypeCode());
        condition.setDatasourceTypeIn(deleteCondition.getTypeCodeIn());
        return datasourceConfigKeyMapper.delete(condition);
    }
}
