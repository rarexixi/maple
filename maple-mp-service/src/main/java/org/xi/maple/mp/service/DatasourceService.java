package org.xi.maple.mp.service;

import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.DatasourceQueryReq;
import org.xi.maple.mp.model.request.DatasourceSaveReq;
import org.xi.maple.mp.model.response.DatasourceDetailResp;
import org.xi.maple.mp.model.response.DatasourceItemResp;

import java.util.List;

/**
 * 数据源业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface DatasourceService {

    /**
     * 添加数据源
     *
     * @param createReq 数据源
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceDetailResp create(DatasourceSaveReq createReq);

    /**
     * 批量添加数据源
     *
     * @param list 数据源列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchCreate(List<DatasourceSaveReq> list);

    // region 删除/启用/禁用

    /**
     * 删除数据源
     *
     * @param idList Id列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteById(List<Integer> idList, BaseEntity entity);

    /**
     * 禁用数据源
     *
     * @param idList Id列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disableById(List<Integer> idList, BaseEntity entity);

    /**
     * 启用数据源
     *
     * @param idList Id列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enableById(List<Integer> idList, BaseEntity entity);

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新数据源非空字段
     *
     * @param id Id
     * @param saveReq 保存数据源请求实体
     * @return 更新后的数据源详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceDetailResp patchById(Integer id, DatasourceSaveReq saveReq);

    /**
     * 根据更新数据源所有字段
     *
     * @param id Id
     * @param saveReq 保存数据源请求实体
     * @return 更新后的数据源详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceDetailResp updateById(Integer id, DatasourceSaveReq saveReq);

    // endregion 更新

    // region 详情

    /**
     * 根据获取数据源详情
     *
     * @param id Id
     * @return 数据源详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceDetailResp getById(Integer id);

    // endregion 详情

    /**
     * 获取数据源列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的数据源列表
     */
    List<DatasourceItemResp> getList(DatasourceQueryReq queryReq);

    /**
     * 分页获取数据源列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的数据源分页列表
     */
    PageList<DatasourceItemResp> getPageList(DatasourceQueryReq queryReq, Integer pageNum, Integer pageSize);
}
