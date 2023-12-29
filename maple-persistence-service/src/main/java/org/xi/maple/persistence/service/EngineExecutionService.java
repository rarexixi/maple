package org.xi.maple.persistence.service;

import org.xi.maple.common.model.PageList;
import org.xi.maple.persistence.model.request.*;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionListItemResponse;

import java.util.Collection;
import java.util.List;

/**
 * 引擎执行记录业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface EngineExecutionService {

    /**
     * 添加引擎执行记录
     *
     * @param addRequest 引擎执行记录
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    Integer add(EngineExecutionAddRequest addRequest);

    /**
     * 批量添加引擎执行记录
     *
     * @param list 引擎执行记录列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    List<Integer> batchAdd(Collection<EngineExecutionAddRequest> list);


    /**
     * 根据执行ID更新引擎执行状态
     *
     * @param id            引擎执行记录ID
     * @param updateRequest 更新引擎执行记录请求实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int updateStatusById(int id, EngineExecutionUpdateStatusRequest updateRequest);

    /**
     * 根据执行ID更新引擎执行信息
     *
     * @param id            引擎执行记录ID
     * @param updateRequest 更新引擎执行记录请求实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int updateExtInfoById(int id, EngineExecutionUpdateRequest updateRequest);

    /**
     * 根据执行ID获取引擎执行记录详情
     *
     * @param id 执行ID
     * @return 引擎执行记录详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    EngineExecutionDetailResponse getById(Integer id);

    /**
     * 分页获取引擎执行记录列表
     *
     * @param queryRequest 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的引擎执行记录分页列表
     */
    PageList<EngineExecutionListItemResponse> getPageList(EngineExecutionQueryRequest queryRequest, Integer pageNum, Integer pageSize);
}
