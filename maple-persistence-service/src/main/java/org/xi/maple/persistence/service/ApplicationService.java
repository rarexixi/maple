package org.xi.maple.persistence.service;

import org.xi.maple.persistence.model.response.ApplicationDetailResp;

/**
 * 访问程序业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface ApplicationService {

    /**
     * 根据应用名称获取访问程序详情
     *
     * @param appName 应用名称
     * @return 访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    ApplicationDetailResp getByAppName(String appName);
}
