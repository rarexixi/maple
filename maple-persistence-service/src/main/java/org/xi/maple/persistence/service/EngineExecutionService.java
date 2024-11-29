package org.xi.maple.persistence.service;

import org.xi.maple.common.model.PageList;
import org.xi.maple.persistence.model.request.EngineExecutionQueryReq;
import org.xi.maple.persistence.model.request.EngineExecutionSaveReq;
import org.xi.maple.persistence.model.request.EngineExecutionExtUpdateReq;
import org.xi.maple.persistence.model.request.EngineExecutionStatusUpdateReq;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;
import org.xi.maple.persistence.model.response.EngineExecutionItemResp;

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
     * @param createReq 引擎执行记录
     * @return 执行ID
     * @author 郗世豪（rarexixi@gmail.com）
     */
    Integer create(EngineExecutionSaveReq createReq);

    /**
     * 批量添加引擎执行记录
     *
     * @param list 引擎执行记录列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    List<Integer> batchCreate(List<EngineExecutionSaveReq> list);

    /**
     * 根据更新引擎执行记录
     *
     * @param id 执行ID
     * @param saveReq 保存引擎执行记录请求实体
     * @return 更新后的引擎执行记录详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    EngineExecutionDetailResp patchById(Integer id, EngineExecutionSaveReq saveReq);

    /**
     * 根据执行ID更新引擎执行状态
     *
     * @param id            引擎执行记录ID
     * @param updateRequest 更新引擎执行记录请求实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int updateStatusById(int id, EngineExecutionStatusUpdateReq updateRequest);

    /**
     * 根据执行ID更新引擎执行信息
     *
     * @param id            引擎执行记录ID
     * @param updateRequest 更新引擎执行记录请求实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int patchExtInfoById(int id, EngineExecutionExtUpdateReq updateRequest);

    /**
     * 根据获取引擎执行记录详情
     *
     * @param id 执行ID
     * @return 引擎执行记录详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    EngineExecutionDetailResp getById(Integer id);

    /**
     * 分页获取引擎执行记录列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的引擎执行记录分页列表
     */
    PageList<EngineExecutionItemResp> getPageList(EngineExecutionQueryReq queryReq, Integer pageNum, Integer pageSize);
}
