package org.xi.maple.persistence.service;

import org.xi.maple.persistence.model.request.EngineExecutionCreateReq;
import org.xi.maple.persistence.model.request.EngineExecutionPatchReq;
import org.xi.maple.persistence.model.request.EngineExecutionStatusUpdateReq;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;

import java.util.Map;

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
    Integer create(EngineExecutionCreateReq createReq);

    /**
     * 根据执行ID更新引擎执行记录
     *
     * @param id      执行ID
     * @param saveReq 保存引擎执行记录请求实体
     * @return 更新后的引擎执行记录详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int patchById(Integer id, EngineExecutionPatchReq saveReq);

    /**
     * 根据执行ID更新引擎执行状态
     *
     * @param id        引擎执行记录ID
     * @param updateReq 更新引擎执行记录请求实体
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int updateStatusById(int id, EngineExecutionStatusUpdateReq updateReq);

    /**
     * 根据执行ID更新引擎执行信息
     *
     * @param id        引擎执行记录ID
     * @param updateReq 引擎执行扩展信息
     * @return 影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int patchExecInfoById(Integer id, Map<String, ?> updateReq);

    /**
     * 根据获取引擎执行记录详情
     *
     * @param id 执行ID
     * @return 引擎执行记录详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    EngineExecutionDetailResp getById(Integer id);
}
