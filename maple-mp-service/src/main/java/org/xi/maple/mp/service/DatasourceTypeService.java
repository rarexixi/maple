package org.xi.maple.mp.service;

import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.DatasourceTypeQueryReq;
import org.xi.maple.mp.model.request.DatasourceTypeSaveReq;
import org.xi.maple.mp.model.response.DatasourceTypeDetailResp;
import org.xi.maple.mp.model.response.DatasourceTypeItemResp;

import java.util.List;

/**
 * 数据源类型业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface DatasourceTypeService {

    /**
     * 添加数据源类型
     *
     * @param createReq 数据源类型
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceTypeDetailResp create(DatasourceTypeSaveReq createReq);

    /**
     * 批量添加数据源类型
     *
     * @param list 数据源类型列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchCreate(List<DatasourceTypeSaveReq> list);

    // region 删除/启用/禁用

    /**
     * 删除数据源类型
     *
     * @param typeCodeList 类型编码列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteByTypeCode(List<String> typeCodeList, BaseEntity entity);

    /**
     * 禁用数据源类型
     *
     * @param typeCodeList 类型编码列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disableByTypeCode(List<String> typeCodeList, BaseEntity entity);

    /**
     * 启用数据源类型
     *
     * @param typeCodeList 类型编码列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enableByTypeCode(List<String> typeCodeList, BaseEntity entity);

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新数据源类型非空字段
     *
     * @param typeCode 类型编码
     * @param saveReq 保存数据源类型请求实体
     * @return 更新后的数据源类型详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceTypeDetailResp patchByTypeCode(String typeCode, DatasourceTypeSaveReq saveReq);

    /**
     * 根据更新数据源类型所有字段
     *
     * @param typeCode 类型编码
     * @param saveReq 保存数据源类型请求实体
     * @return 更新后的数据源类型详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceTypeDetailResp updateByTypeCode(String typeCode, DatasourceTypeSaveReq saveReq);

    // endregion 更新

    // region 详情

    /**
     * 根据获取数据源类型详情
     *
     * @param typeCode 类型编码
     * @return 数据源类型详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    DatasourceTypeDetailResp getByTypeCode(String typeCode);

    // endregion 详情

    /**
     * 获取数据源类型列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的数据源类型列表
     */
    List<DatasourceTypeItemResp> getList(DatasourceTypeQueryReq queryReq);

    /**
     * 分页获取数据源类型列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的数据源类型分页列表
     */
    PageList<DatasourceTypeItemResp> getPageList(DatasourceTypeQueryReq queryReq, Integer pageNum, Integer pageSize);
}
