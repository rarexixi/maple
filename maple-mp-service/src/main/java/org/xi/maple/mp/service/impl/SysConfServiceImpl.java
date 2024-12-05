package org.xi.maple.mp.service.impl;

import org.xi.maple.common.constant.ValidConstant;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.service.util.ObjectUtils;
import org.xi.maple.mp.persistence.condition.SysConfFilterCondition;
import org.xi.maple.mp.persistence.condition.SysConfPkCondition;
import org.xi.maple.mp.persistence.entity.SysConfEntity;
import org.xi.maple.mp.persistence.entity.SysConfEntityExt;
import org.xi.maple.mp.persistence.mapper.SysConfMapper;
import org.xi.maple.mp.model.request.SysConfQueryReq;
import org.xi.maple.mp.model.request.SysConfSaveReq;
import org.xi.maple.mp.model.response.SysConfDetailResp;
import org.xi.maple.mp.model.response.SysConfItemResp;
import org.xi.maple.mp.service.SysConfService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统配置业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
@Service("sysConfService")
public class SysConfServiceImpl implements SysConfService {

    final SysConfMapper sysConfMapper;

    @Autowired
    public SysConfServiceImpl(SysConfMapper sysConfMapper) {
        this.sysConfMapper = sysConfMapper;
    }

    /**
     * 添加系统配置
     *
     * @param createReq 系统配置
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public SysConfDetailResp create(SysConfSaveReq createReq) {
        SysConfEntity entity = ObjectUtils.copy(createReq, SysConfEntity.class);
        sysConfMapper.insert(entity);
        return getByConfKey(entity.getConfKey());
    }

    /**
     * 批量添加系统配置
     *
     * @param list 系统配置列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int batchCreate(List<SysConfSaveReq> list) {
        List<SysConfEntity> entityList = ObjectUtils.copy(list, SysConfEntity.class);
        return sysConfMapper.batchInsert(entityList);
    }

    // region 删除/启用/禁用

    /**
     * 删除系统配置
     *
     * @param confKeyList 配置键列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int deleteByConfKey(List<String> confKeyList, BaseEntity baseEntity) {
        SysConfPkCondition condition = getPkCondition(confKeyList);
        return sysConfMapper.deleteByCondition(condition);
    }

    /**
     * 禁用系统配置
     *
     * @param confKeyList 配置键列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int disableByConfKey(List<String> confKeyList, BaseEntity baseEntity) {
        SysConfPkCondition condition = getPkCondition(confKeyList);
        SysConfEntity entity = ObjectUtils.copy(baseEntity, SysConfEntity.class);
        entity.setDisabled(ValidConstant.INVALID);
        return sysConfMapper.patchByCondition(condition, entity);
    }

    /**
     * 启用系统配置
     *
     * @param confKeyList 配置键列表
     * @param baseEntity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public int enableByConfKey(List<String> confKeyList, BaseEntity baseEntity) {
        SysConfPkCondition condition = getPkCondition(confKeyList);
        SysConfEntity entity = ObjectUtils.copy(baseEntity, SysConfEntity.class);
        entity.setDisabled(ValidConstant.VALID);
        return sysConfMapper.patchByCondition(condition, entity);
    }

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新系统配置非空字段
     *
     * @param confKey 配置键
     * @param saveReq 保存系统配置请求实体
     * @return 更新后的系统配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public SysConfDetailResp patchByConfKey(String confKey, SysConfSaveReq saveReq) {
        SysConfPkCondition condition = getPkCondition(confKey);
        SysConfEntity entity = ObjectUtils.copy(saveReq, SysConfEntity.class);
        sysConfMapper.patchByCondition(condition, entity);
        return getByConfKey(confKey);
    }

    /**
     * 根据更新系统配置所有字段
     *
     * @param confKey 配置键
     * @param saveReq 保存系统配置请求实体
     * @return 更新后的系统配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    @Transactional
    public SysConfDetailResp updateByConfKey(String confKey, SysConfSaveReq saveReq) {
        SysConfEntity entity = ObjectUtils.copy(saveReq, SysConfEntity.class);
        sysConfMapper.updateByConfKey(confKey, entity);
        return getByConfKey(confKey);
    }

    // endregion 更新

    // region 详情

    /**
     * 根据获取系统配置详情
     *
     * @param confKey 配置键
     * @return 系统配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    @Override
    public SysConfDetailResp getByConfKey(String confKey) {
        SysConfEntityExt entity = sysConfMapper.getByConfKey(confKey);
        if (entity == null) {
            throw new MapleDataNotFoundException("系统配置不存在");
        }
        return ObjectUtils.copy(entity, SysConfDetailResp.class);
    }

    // endregion 详情

    /**
     * 获取系统配置列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的系统配置列表
     */
    @Override
    public List<SysConfItemResp> getList(SysConfQueryReq queryReq) {
        SysConfFilterCondition condition = ObjectUtils.copy(queryReq, SysConfFilterCondition.class);
        List<SysConfEntity> list = sysConfMapper.select(condition, null, queryReq.getSort());
        return ObjectUtils.copy(list, SysConfItemResp.class);
    }

    /**
     * 分页获取系统配置列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的系统配置分页列表
     */
    @Override
    public PageList<SysConfItemResp> getPageList(SysConfQueryReq queryReq, Integer pageNum, Integer pageSize) {

        SysConfFilterCondition condition = ObjectUtils.copy(queryReq, SysConfFilterCondition.class);
        ISelect select = () -> sysConfMapper.select(condition, null, queryReq.getSort());
        PageInfo<SysConfEntityExt> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);

        List<SysConfItemResp> list = ObjectUtils.copy(pageInfo.getList(), SysConfItemResp.class);
        return new PageList<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), list);
    }

    private SysConfPkCondition getPkCondition(String confKey) {
        SysConfPkCondition condition = new SysConfPkCondition();
        condition.setConfKey(confKey);
        return condition;
    }

    private SysConfPkCondition getPkCondition(List<String> confKeyList) {
        SysConfPkCondition condition = new SysConfPkCondition();
        if (confKeyList.isEmpty()) {
            return null;
        } else if (confKeyList.size() == 1) {
            condition.setConfKey(confKeyList.get(0));
        } else {
            condition.setConfKeyIn(confKeyList);
        }
        return condition;
    }
}
