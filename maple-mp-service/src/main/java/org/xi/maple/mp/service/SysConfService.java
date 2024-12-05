package org.xi.maple.mp.service;

import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.SysConfQueryReq;
import org.xi.maple.mp.model.request.SysConfSaveReq;
import org.xi.maple.mp.model.response.SysConfDetailResp;
import org.xi.maple.mp.model.response.SysConfItemResp;

import java.util.List;

/**
 * 系统配置业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface SysConfService {

    /**
     * 添加系统配置
     *
     * @param createReq 系统配置
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    SysConfDetailResp create(SysConfSaveReq createReq);

    /**
     * 批量添加系统配置
     *
     * @param list 系统配置列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchCreate(List<SysConfSaveReq> list);

    // region 删除/启用/禁用

    /**
     * 删除系统配置
     *
     * @param confKeyList 配置键列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteByConfKey(List<String> confKeyList, BaseEntity entity);

    /**
     * 禁用系统配置
     *
     * @param confKeyList 配置键列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disableByConfKey(List<String> confKeyList, BaseEntity entity);

    /**
     * 启用系统配置
     *
     * @param confKeyList 配置键列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enableByConfKey(List<String> confKeyList, BaseEntity entity);

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
    SysConfDetailResp patchByConfKey(String confKey, SysConfSaveReq saveReq);

    /**
     * 根据更新系统配置所有字段
     *
     * @param confKey 配置键
     * @param saveReq 保存系统配置请求实体
     * @return 更新后的系统配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    SysConfDetailResp updateByConfKey(String confKey, SysConfSaveReq saveReq);

    // endregion 更新

    // region 详情

    /**
     * 根据获取系统配置详情
     *
     * @param confKey 配置键
     * @return 系统配置详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    SysConfDetailResp getByConfKey(String confKey);

    // endregion 详情

    /**
     * 获取系统配置列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的系统配置列表
     */
    List<SysConfItemResp> getList(SysConfQueryReq queryReq);

    /**
     * 分页获取系统配置列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的系统配置分页列表
     */
    PageList<SysConfItemResp> getPageList(SysConfQueryReq queryReq, Integer pageNum, Integer pageSize);
}
