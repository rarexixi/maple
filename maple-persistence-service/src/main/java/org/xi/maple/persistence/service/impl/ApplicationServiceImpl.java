package org.xi.maple.persistence.service.impl;

import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.persistence.persistence.entity.ApplicationEntityExt;
import org.xi.maple.persistence.persistence.mapper.ApplicationMapper;
import org.xi.maple.persistence.model.response.ApplicationDetailResp;
import org.xi.maple.persistence.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 访问程序业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {

    final ApplicationMapper applicationMapper;

    @Autowired
    public ApplicationServiceImpl(ApplicationMapper applicationMapper) {
        this.applicationMapper = applicationMapper;
    }


    // region 详情

    /**
     * 根据应用名称获取访问程序详情
     *
     * @param appName 应用名称
     * @return 访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Cacheable(cacheNames = {"maple-app"}, key = "#appName")
    @Override
    public ApplicationDetailResp getByAppName(String appName) {
        ApplicationEntityExt entity = applicationMapper.getByAppName(appName);
        if (entity == null) {
            throw new MapleDataNotFoundException("访问程序不存在");
        }
        return ObjectUtils.copy(entity, ApplicationDetailResp.class);
    }

    // endregion 详情
}
