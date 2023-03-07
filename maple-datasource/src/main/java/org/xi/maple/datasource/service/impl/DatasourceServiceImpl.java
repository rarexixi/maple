package org.xi.maple.datasource.service.impl;

import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.models.PageList;
import org.xi.maple.common.utils.ObjectUtils;
import org.xi.maple.datasource.constant.DeletedConstant;
import org.xi.maple.datasource.presentation.condition.DatasourceSelectCondition;
import org.xi.maple.datasource.presentation.condition.DatasourceUpdateCondition;
import org.xi.maple.datasource.presentation.entity.DatasourceEntity;
import org.xi.maple.datasource.presentation.entity.DatasourceEntityExt;
import org.xi.maple.datasource.presentation.mapper.DatasourceMapper;
import org.xi.maple.datasource.model.request.DatasourceAddRequest;
import org.xi.maple.datasource.model.request.DatasourcePatchRequest;
import org.xi.maple.datasource.model.request.DatasourceQueryRequest;
import org.xi.maple.datasource.model.request.DatasourceSaveRequest;
import org.xi.maple.datasource.model.response.DatasourceDetailResponse;
import org.xi.maple.datasource.model.response.DatasourceListItemResponse;
import org.xi.maple.datasource.service.DatasourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 数据源配置业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("datasourceService")
@Transactional
public class DatasourceServiceImpl implements DatasourceService {

    final DatasourceMapper datasourceMapper;

    @Autowired
    public DatasourceServiceImpl(DatasourceMapper datasourceMapper) {
        this.datasourceMapper = datasourceMapper;
    }

    /**
     * 添加数据源配置
     *
     * @param addRequest 数据源配置
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public DatasourceDetailResponse add(DatasourceAddRequest addRequest) {
        DatasourceEntity entity = ObjectUtils.copy(addRequest, DatasourceEntity.class);
        datasourceMapper.insert(entity);
        return getById(entity.getId());
    }

    /**
     * 批量添加数据源配置
     *
     * @param list 数据源配置列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public int batchAdd(Collection<DatasourceAddRequest> list) {
        List<DatasourceEntity> entityList = ObjectUtils.copy(list, DatasourceEntity.class);
        return datasourceMapper.batchInsert(entityList);
    }

    /**
     * 删除数据源配置
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public int delete(DatasourcePatchRequest patchRequest) {
        DatasourceSelectCondition condition = ObjectUtils.copy(patchRequest, DatasourceSelectCondition.class);
        return datasourceMapper.delete(condition);
    }

    /**
     * 禁用数据源配置
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public int disable(DatasourcePatchRequest patchRequest) {
        DatasourceUpdateCondition condition = ObjectUtils.copy(patchRequest, DatasourceUpdateCondition.class);
        DatasourceEntity entity = ObjectUtils.copy(patchRequest, DatasourceEntity.class, "id");
        entity.setDeleted(DeletedConstant.INVALID);
        return datasourceMapper.update(entity, condition);
    }

    /**
     * 启用数据源配置
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public int enable(DatasourcePatchRequest patchRequest) {
        DatasourceUpdateCondition condition = ObjectUtils.copy(patchRequest, DatasourceUpdateCondition.class);
        DatasourceEntity entity = ObjectUtils.copy(patchRequest, DatasourceEntity.class, "id");
        entity.setDeleted(DeletedConstant.VALID);
        return datasourceMapper.update(entity, condition);
    }

    /**
     * 根据Id更新数据源配置
     *
     * @param saveRequest 保存数据源配置请求实体
     * @return 更新后的数据源配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public DatasourceDetailResponse updateById(DatasourceSaveRequest saveRequest) {
        DatasourceUpdateCondition condition = new DatasourceUpdateCondition();
        condition.setId(saveRequest.getId());
        DatasourceEntity entity = ObjectUtils.copy(saveRequest, DatasourceEntity.class);
        datasourceMapper.update(entity, condition);
        return getById(saveRequest.getId());
    }

    /**
     * 根据Id获取数据源配置详情
     *
     * @param id Id
     * @return 数据源配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional(readOnly = true)
    public DatasourceDetailResponse getById(Integer id) {
        DatasourceEntityExt entity = datasourceMapper.detail(id);
        if (entity == null) {
            throw new MapleDataNotFoundException("数据源配置不存在");
        }
        return ObjectUtils.copy(entity, DatasourceDetailResponse.class);
    }

    /**
     * 获取数据源配置列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的数据源配置列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<DatasourceListItemResponse> getList(DatasourceQueryRequest queryRequest) {
        DatasourceSelectCondition condition = ObjectUtils.copy(queryRequest, DatasourceSelectCondition.class);
        List<DatasourceEntityExt> list = datasourceMapper.select(condition);
        return ObjectUtils.copy(list, DatasourceListItemResponse.class);
    }

    /**
     * 分页获取数据源配置列表
     *
     * @param queryRequest 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的数据源配置分页列表
     */
    @Override
    @Transactional(readOnly = true)
    public PageList<DatasourceListItemResponse> getPageList(DatasourceQueryRequest queryRequest, Integer pageNum, Integer pageSize) {

        DatasourceSelectCondition condition = ObjectUtils.copy(queryRequest, DatasourceSelectCondition.class);
        PageInfo<DatasourceEntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> datasourceMapper.select(condition));

        List<DatasourceListItemResponse> list = ObjectUtils.copy(pageInfo.getList(), DatasourceListItemResponse.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }
}
