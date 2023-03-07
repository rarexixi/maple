package org.xi.maple.datasource.service;

import org.xi.maple.common.models.PageList;
import org.xi.maple.datasource.model.request.DatasourceAddRequest;
import org.xi.maple.datasource.model.request.DatasourcePatchRequest;
import org.xi.maple.datasource.model.request.DatasourceQueryRequest;
import org.xi.maple.datasource.model.request.DatasourceSaveRequest;
import org.xi.maple.datasource.model.response.DatasourceDetailResponse;
import org.xi.maple.datasource.model.response.DatasourceListItemResponse;

import java.util.Collection;
import java.util.List;

/**
 * 数据源配置业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface DatasourceService {

    /**
     * 添加数据源配置
     *
     * @param addRequest 数据源配置
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceDetailResponse add(DatasourceAddRequest addRequest);

    /**
     * 批量添加数据源配置
     *
     * @param list 数据源配置列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchAdd(Collection<DatasourceAddRequest> list);

    /**
     * 删除数据源配置
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int delete(DatasourcePatchRequest patchRequest);

    /**
     * 禁用数据源配置
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disable(DatasourcePatchRequest patchRequest);

    /**
     * 启用数据源配置
     *
     * @param patchRequest 更新条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enable(DatasourcePatchRequest patchRequest);

    /**
     * 根据Id更新数据源配置
     *
     * @param saveRequest 保存数据源配置请求实体
     * @return 更新后的数据源配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceDetailResponse updateById(DatasourceSaveRequest saveRequest);

    /**
     * 根据Id获取数据源配置详情
     *
     * @param id Id
     * @return 数据源配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceDetailResponse getById(Integer id);

    /**
     * 获取数据源配置列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的数据源配置列表
     */
    List<DatasourceListItemResponse> getList(DatasourceQueryRequest queryRequest);

    /**
     * 分页获取数据源配置列表
     *
     * @param queryRequest 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的数据源配置分页列表
     */
    PageList<DatasourceListItemResponse> getPageList(DatasourceQueryRequest queryRequest, Integer pageNum, Integer pageSize);
}
