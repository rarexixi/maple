package org.xi.maple.mp.service;

import org.xi.maple.common.model.PageList;
import org.xi.maple.common.model.BaseEntity;
import org.xi.maple.mp.model.request.JobQueryReq;
import org.xi.maple.mp.model.request.JobSaveReq;
import org.xi.maple.mp.model.response.JobDetailResp;
import org.xi.maple.mp.model.response.JobItemResp;

import java.util.List;

/**
 * 执行作业业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface JobService {

    /**
     * 添加执行作业
     *
     * @param createReq 执行作业
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    JobDetailResp create(JobSaveReq createReq);

    /**
     * 批量添加执行作业
     *
     * @param list 执行作业列表
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int batchCreate(List<JobSaveReq> list);

    // region 删除/启用/禁用

    /**
     * 删除执行作业
     *
     * @param idList 作业ID列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int deleteById(List<Integer> idList, BaseEntity entity);

    /**
     * 禁用执行作业
     *
     * @param idList 作业ID列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int disableById(List<Integer> idList, BaseEntity entity);

    /**
     * 启用执行作业
     *
     * @param idList 作业ID列表
     * @param entity
     * @return 受影响的行数
     * @author 郗世豪（rarexixi@gmail.com）
     */
    int enableById(List<Integer> idList, BaseEntity entity);

    // endregion 删除/启用/禁用

    // region 更新

    /**
     * 根据更新执行作业非空字段
     *
     * @param id 作业ID
     * @param saveReq 保存执行作业请求实体
     * @return 更新后的执行作业详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    JobDetailResp patchById(Integer id, JobSaveReq saveReq);

    /**
     * 根据更新执行作业所有字段
     *
     * @param id 作业ID
     * @param saveReq 保存执行作业请求实体
     * @return 更新后的执行作业详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    JobDetailResp updateById(Integer id, JobSaveReq saveReq);

    // endregion 更新

    // region 详情

    /**
     * 根据获取执行作业详情
     *
     * @param id 作业ID
     * @return 执行作业详情
     * @author 郗世豪（rarexixi@gmail.com）
     */
    JobDetailResp getById(Integer id);

    // endregion 详情

    /**
     * 获取执行作业列表
     *
     * @param queryReq 搜索条件
     * @return 符合条件的执行作业列表
     */
    List<JobItemResp> getList(JobQueryReq queryReq);

    /**
     * 分页获取执行作业列表
     *
     * @param queryReq 搜索条件
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @return 符合条件的执行作业分页列表
     */
    PageList<JobItemResp> getPageList(JobQueryReq queryReq, Integer pageNum, Integer pageSize);
}
