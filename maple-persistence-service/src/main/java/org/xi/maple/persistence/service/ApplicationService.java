package org.xi.maple.persistence.service;

import org.xi.maple.common.model.PageList;
import org.xi.maple.persistence.model.request.ApplicationAddRequest;
import org.xi.maple.persistence.model.request.ApplicationPatchRequest;
import org.xi.maple.persistence.model.request.ApplicationQueryRequest;
import org.xi.maple.persistence.model.request.ApplicationSaveRequest;
import org.xi.maple.persistence.model.response.ApplicationDetailResponse;
import org.xi.maple.persistence.model.response.ApplicationListItemResponse;

import java.util.Collection;
import java.util.List;

/**
 * 访问程序业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface ApplicationService {

    /**
     * 添加访问程序
     *
     * @param addRequest 访问程序
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ApplicationDetailResponse add(ApplicationAddRequest addRequest);

    /**
     * 批量添加访问程序
     *
     * @param list 访问程序列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchAdd(Collection<ApplicationAddRequest> list);

    /**
     * 删除访问程序
     *
     * @param patchRequest 删除条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int delete(ApplicationPatchRequest patchRequest);

    /**
     * 禁用访问程序
     *
     * @param patchRequest 禁用条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disable(ApplicationPatchRequest patchRequest);

    /**
     * 启用访问程序
     *
     * @param patchRequest 启用条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enable(ApplicationPatchRequest patchRequest);

    /**
     * 根据应用名称更新访问程序
     *
     * @param saveRequest 保存访问程序请求实体
     * @param appName 应用名称
     * @return 更新后的访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ApplicationDetailResponse updateByAppName(ApplicationSaveRequest saveRequest, String appName);

    /**
     * 根据应用名称获取访问程序详情
     *
     * @param appName 应用名称
     * @return 访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ApplicationDetailResponse getByAppName(String appName);

    /**
     * 获取访问程序列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的访问程序列表
     */
    List<ApplicationListItemResponse> getList(ApplicationQueryRequest queryRequest);

    /**
     * 分页获取访问程序列表
     *
     * @param queryRequest 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的访问程序分页列表
     */
    PageList<ApplicationListItemResponse> getPageList(ApplicationQueryRequest queryRequest, Integer pageNum, Integer pageSize);
}
