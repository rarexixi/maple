package org.xi.maple.datasource.service;

import org.xi.maple.datasource.model.request.DatasourceTypeAddRequest;
import org.xi.maple.datasource.model.request.DatasourceTypePatchRequest;
import org.xi.maple.datasource.model.request.DatasourceTypeQueryRequest;
import org.xi.maple.datasource.model.request.DatasourceTypeSaveRequest;
import org.xi.maple.datasource.model.response.DatasourceTypeDetailResponse;
import org.xi.maple.datasource.model.response.DatasourceTypeListItemResponse;

import java.util.List;

/**
 * 数据源类型业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface DatasourceTypeService {

    /**
     * 添加数据源类型
     *
     * @param addRequest 数据源类型
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceTypeDetailResponse add(DatasourceTypeAddRequest addRequest);

    /**
     * 删除数据源类型
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int delete(DatasourceTypePatchRequest patchRequest);

    /**
     * 禁用数据源类型
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disable(DatasourceTypePatchRequest patchRequest);

    /**
     * 启用数据源类型
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enable(DatasourceTypePatchRequest patchRequest);

    /**
     * 根据类型编码更新数据源类型
     *
     * @param saveRequest 保存数据源类型请求实体
     * @param typeCode 类型编码
     * @return 更新后的数据源类型详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceTypeDetailResponse updateByTypeCode(DatasourceTypeSaveRequest saveRequest, String typeCode);

    /**
     * 根据类型编码获取数据源类型详情
     *
     * @param typeCode 类型编码
     * @return 数据源类型详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceTypeDetailResponse getByTypeCode(String typeCode);

    /**
     * 获取数据源类型列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的数据源类型列表
     */
    List<DatasourceTypeListItemResponse> getList(DatasourceTypeQueryRequest queryRequest);
}
