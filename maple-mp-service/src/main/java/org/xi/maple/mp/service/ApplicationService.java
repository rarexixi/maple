package org.xi.maple.mp.service;

import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.ApplicationQueryReq;
import org.xi.maple.mp.model.request.ApplicationSaveReq;
import org.xi.maple.mp.model.response.ApplicationDetailResp;
import org.xi.maple.mp.model.response.ApplicationItemResp;

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
     * @param createReq 访问程序
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ApplicationDetailResp create(ApplicationSaveReq createReq);

    /**
     * 批量添加访问程序
     *
     * @param list 访问程序列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchCreate(List<ApplicationSaveReq> list);

    // region 删除/启用/禁用

    /**
     * 删除访问程序
     *
     * @param appNameList 应用名称列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteByAppName(List<String> appNameList, BaseEntity entity);

    /**
     * 禁用访问程序
     *
     * @param appNameList 应用名称列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disableByAppName(List<String> appNameList, BaseEntity entity);

    /**
     * 启用访问程序
     *
     * @param appNameList 应用名称列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enableByAppName(List<String> appNameList, BaseEntity entity);

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新访问程序非空字段
     *
     * @param appName 应用名称
     * @param saveReq 保存访问程序请求实体
     * @return 更新后的访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ApplicationDetailResp patchByAppName(String appName, ApplicationSaveReq saveReq);

    /**
     * 根据更新访问程序所有字段
     *
     * @param appName 应用名称
     * @param saveReq 保存访问程序请求实体
     * @return 更新后的访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ApplicationDetailResp updateByAppName(String appName, ApplicationSaveReq saveReq);

    // endregion 更新

    // region 详情

    /**
     * 根据获取访问程序详情
     *
     * @param appName 应用名称
     * @return 访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ApplicationDetailResp getByAppName(String appName);

    // endregion 详情

    /**
     * 获取访问程序列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的访问程序列表
     */
    List<ApplicationItemResp> getList(ApplicationQueryReq queryReq);

    /**
     * 分页获取访问程序列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的访问程序分页列表
     */
    PageList<ApplicationItemResp> getPageList(ApplicationQueryReq queryReq, Integer pageNum, Integer pageSize);
}
