package org.xi.maple.persistence.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.xi.maple.common.constant.ValidConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.PageList;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.persistence.persistence.condition.ApplicationFilterCondition;
import org.xi.maple.persistence.persistence.condition.ApplicationPkCondition;
import org.xi.maple.persistence.persistence.entity.ApplicationEntity;
import org.xi.maple.persistence.persistence.entity.ApplicationEntityExt;
import org.xi.maple.persistence.persistence.mapper.ApplicationMapper;
import org.xi.maple.persistence.model.request.ApplicationQueryReq;
import org.xi.maple.persistence.model.request.ApplicationSaveReq;
import org.xi.maple.persistence.model.response.ApplicationDetailResp;
import org.xi.maple.persistence.model.response.ApplicationItemResp;
import org.xi.maple.persistence.service.ApplicationService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param createReq 访问程序
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public ApplicationDetailResp create(ApplicationSaveReq createReq) {
        ApplicationEntity entity = ObjectUtils.copy(createReq, ApplicationEntity.class);
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
    public int batchCreate(List<ApplicationSaveReq> list) {
        List<ApplicationEntity> entityList = ObjectUtils.copy(list, ApplicationEntity.class);
        return applicationMapper.batchInsert(entityList);
    }

    // region 删除/启用/禁用

    /**
     * 删除访问程序
     *
     * @param appName    应用名称
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-app"}, key = "#appName")
    @Override
    @Transactional
    public int deleteByAppName(String appName, BaseEntity baseEntity) {
        ApplicationPkCondition condition = getPkCondition(appName);
        return applicationMapper.deleteByCondition(condition);
    }

    /**
     * 禁用访问程序
     *
     * @param appName    应用名称
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-app"}, key = "#appName")
    @Override
    @Transactional
    public int disableByAppName(String appName, BaseEntity baseEntity) {
        ApplicationPkCondition condition = getPkCondition(appName);
        ApplicationEntity entity = ObjectUtils.copy(baseEntity, ApplicationEntity.class);
        entity.setDisabled(ValidConstant.INVALID);
        return applicationMapper.patchByCondition(condition, entity);
    }

    /**
     * 启用访问程序
     *
     * @param appName    应用名称
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-app"}, key = "#appName")
    @Override
    @Transactional
    public int enableByAppName(String appName, BaseEntity baseEntity) {
        ApplicationPkCondition condition = getPkCondition(appName);
        ApplicationEntity entity = ObjectUtils.copy(baseEntity, ApplicationEntity.class);
        entity.setDisabled(ValidConstant.VALID);
        return applicationMapper.patchByCondition(condition, entity);
    }

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新访问程序
     *
     * @param appName     应用名称
     * @param saveReq 保存访问程序请求实体
     * @return 更新后的访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-app"}, key = "#appName")
    @Override
    @Transactional
    public ApplicationDetailResp patchByAppName(String appName, ApplicationSaveReq saveReq) {
        ApplicationPkCondition condition = getPkCondition(appName);
        ApplicationEntity entity = ObjectUtils.copy(saveReq, ApplicationEntity.class);
        applicationMapper.patchByCondition(condition, entity);
        return getByAppName(appName);
    }

    /**
     * 根据更新访问程序
     *
     * @param appName     应用名称
     * @param saveReq 保存访问程序请求实体
     * @return 更新后的访问程序详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @CacheEvict(cacheNames = {"maple-app"}, key = "#appName")
    @Override
    @Transactional
    public ApplicationDetailResp updateByAppName(String appName, ApplicationSaveReq saveReq) {
        ApplicationEntity entity = ObjectUtils.copy(saveReq, ApplicationEntity.class);
        applicationMapper.updateByAppName(appName, entity);
        return getByAppName(appName);
    }

    // endregion 更新

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

    /**
     * 获取访问程序列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的访问程序列表
     */
    @Override
    public List<ApplicationItemResp> getList(ApplicationQueryReq queryReq) {
        ApplicationFilterCondition condition = ObjectUtils.copy(queryReq, ApplicationFilterCondition.class);
        List<ApplicationEntity> list = applicationMapper.select(condition, null, queryReq.getSort());
        return ObjectUtils.copy(list, ApplicationItemResp.class);
    }

    /**
     * 分页获取访问程序列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的访问程序分页列表
     */
    @Override
    public PageList<ApplicationItemResp> getPageList(ApplicationQueryReq queryReq, Integer pageNum, Integer pageSize) {

        ApplicationFilterCondition condition = ObjectUtils.copy(queryReq, ApplicationFilterCondition.class);
        ISelect select = () -> applicationMapper.select(condition, null, queryReq.getSort());
        PageInfo<ApplicationEntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);

        List<ApplicationItemResp> list = ObjectUtils.copy(pageInfo.getList(), ApplicationItemResp.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }

    private ApplicationPkCondition getPkCondition(String appName) {
        ApplicationPkCondition condition = new ApplicationPkCondition();
        condition.setAppName(appName);
        return condition;
    }
}
