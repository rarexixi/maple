package org.xi.maple.persistence.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.xi.maple.common.constant.DeletedConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.PageList;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.persistence.persistence.condition.ApplicationSelectCondition;
import org.xi.maple.persistence.persistence.entity.ApplicationEntity;
import org.xi.maple.persistence.persistence.entity.ApplicationEntityExt;
import org.xi.maple.persistence.persistence.mapper.ApplicationMapper;
import org.xi.maple.persistence.model.request.ApplicationAddRequest;
import org.xi.maple.persistence.model.request.ApplicationPatchRequest;
import org.xi.maple.persistence.model.request.ApplicationQueryRequest;
import org.xi.maple.persistence.model.request.ApplicationSaveRequest;
import org.xi.maple.persistence.model.response.ApplicationDetailResponse;
import org.xi.maple.persistence.model.response.ApplicationListItemResponse;
import org.xi.maple.persistence.service.ApplicationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

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

    /**
     * 添加访问程序
     *
     * @param addRequest 访问程序
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ApplicationDetailResponse add(ApplicationAddRequest addRequest) {
        ApplicationEntity entity = ObjectUtils.copy(addRequest, ApplicationEntity.class);
        applicationMapper.insert(entity);
        return getByAppName(entity.getAppName());
    }

    /**
     * 批量添加访问程序
     *
     * @param list 访问程序列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int batchAdd(Collection<ApplicationAddRequest> list) {
        List<ApplicationEntity> entityList = ObjectUtils.copy(list, ApplicationEntity.class);
        return applicationMapper.batchInsert(entityList);
    }

    /**
     * 删除访问程序
     *
     * @param patchRequest 删除条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-app"}, key = "#patchRequest.appName")
    @Override
    @Transactional
    public int delete(ApplicationPatchRequest patchRequest) {
        return applicationMapper.deleteByPk(patchRequest.getAppName());
    }

    /**
     * 禁用访问程序
     *
     * @param patchRequest 禁用条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-app"}, key = "#patchRequest.appName")
    @Override
    @Transactional
    public int disable(ApplicationPatchRequest patchRequest) {
        ApplicationEntity entity = ObjectUtils.copy(patchRequest, ApplicationEntity.class, "appName");
        entity.setDeleted(DeletedConstant.INVALID);
        return applicationMapper.updateByPk(entity, patchRequest.getAppName());
    }

    /**
     * 启用访问程序
     *
     * @param patchRequest 启用条件请求
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-app"}, key = "#patchRequest.appName")
    @Override
    @Transactional
    public int enable(ApplicationPatchRequest patchRequest) {
        ApplicationEntity entity = ObjectUtils.copy(patchRequest, ApplicationEntity.class, "appName");
        entity.setDeleted(DeletedConstant.VALID);
        return applicationMapper.updateByPk(entity, patchRequest.getAppName());
    }

    /**
     * 根据应用名称更新访问程序
     *
     * @param saveRequest 保存访问程序请求实体
     * @param appName 应用名称
     * @return 更新后的访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-app"}, key = "#appName")
    @Override
    @Transactional
    public ApplicationDetailResponse updateByAppName(ApplicationSaveRequest saveRequest, String appName) {
        ApplicationEntity entity = ObjectUtils.copy(saveRequest, ApplicationEntity.class);
        applicationMapper.updateByPk(entity, appName);
        ApplicationDetailResponse result;
        if (StringUtils.isBlank(saveRequest.getAppName())) {
            result = getByAppName(appName);
        } else {
            result = getByAppName(saveRequest.getAppName());
        }
        return result;
    }

    /**
     * 根据应用名称获取访问程序详情
     *
     * @param appName 应用名称
     * @return 访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Cacheable(cacheNames = {"maple-app"}, key = "#appName")
    @Override
    @Transactional(readOnly = true)
    public ApplicationDetailResponse getByAppName(String appName) {
        ApplicationEntityExt entity = applicationMapper.detailByPk(appName);
        if (entity == null) {
            throw new MapleDataNotFoundException("访问程序不存在");
        }
        return ObjectUtils.copy(entity, ApplicationDetailResponse.class);
    }

    /**
     * 获取访问程序列表
     *
     * @param queryRequest 搜索条件
     * @return 符合条件的访问程序列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationListItemResponse> getList(ApplicationQueryRequest queryRequest) {
        ApplicationSelectCondition condition = ObjectUtils.copy(queryRequest, ApplicationSelectCondition.class);
        List<ApplicationEntity> list = applicationMapper.select(condition);
        return ObjectUtils.copy(list, ApplicationListItemResponse.class);
    }

    /**
     * 分页获取访问程序列表
     *
     * @param queryRequest 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的访问程序分页列表
     */
    @Override
    @Transactional(readOnly = true)
    public PageList<ApplicationListItemResponse> getPageList(ApplicationQueryRequest queryRequest, Integer pageNum, Integer pageSize) {

        ApplicationSelectCondition condition = ObjectUtils.copy(queryRequest, ApplicationSelectCondition.class);
        PageInfo<ApplicationEntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> applicationMapper.select(condition));

        List<ApplicationListItemResponse> list = ObjectUtils.copy(pageInfo.getList(), ApplicationListItemResponse.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }
}
