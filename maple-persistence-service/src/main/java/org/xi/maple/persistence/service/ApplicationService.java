package org.xi.maple.persistence.service;

import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.model.request.ApplicationQueryReq;
import org.xi.maple.persistence.model.request.ApplicationSaveReq;
import org.xi.maple.persistence.model.response.ApplicationDetailResp;
import org.xi.maple.persistence.model.response.ApplicationItemResp;

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

    /**
     * 删除访问程序
     *
     * @param appName 应用名称
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteByAppName(String appName, BaseEntity entity);

    /**
     * 禁用访问程序
     *
     * @param appName 应用名称
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disableByAppName(String appName, BaseEntity entity);

    /**
     * 启用访问程序
     *
     * @param appName 应用名称
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enableByAppName(String appName, BaseEntity entity);

    /**
     * 根据更新访问程序
     *
     * @param appName 应用名称
     * @param saveReq 保存访问程序请求实体
     * @return 更新后的访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ApplicationDetailResp patchByAppName(String appName, ApplicationSaveReq saveReq);

    /**
     * 根据应用名称更新访问程序
     *
     * @param appName 应用名称
     * @param saveReq 保存访问程序请求实体
     * @return 更新后的访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ApplicationDetailResp updateByAppName(String appName, ApplicationSaveReq saveReq);

    /**
     * 根据应用名称获取访问程序详情
     *
     * @param appName 应用名称
     * @return 访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ApplicationDetailResp getByAppName(String appName);

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
