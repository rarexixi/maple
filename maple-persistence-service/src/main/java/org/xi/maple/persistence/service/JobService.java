package org.xi.maple.persistence.service;

import org.xi.maple.persistence.model.response.JobDetailResp;

/**
 * 执行作业业务逻辑
 *
 * @author 郗世豪（rarexixi@gmail.com）
 */
public interface JobService {

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
}
