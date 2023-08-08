package org.xi.maple.datacalc.api.service;

import org.xi.maple.datacalc.api.model.request.SubmitJobRequest;
import org.xi.maple.datacalc.api.model.response.JobDetailResponse;

/**
 * @author xishihao
 */
public interface JobService {

    /**
     * 获取作业状态
     *
     * @param jobId 作业ID
     * @return 作业状态
     */
    String getJobStatus(Integer jobId);

    /**
     * 获取作业详情
     *
     * @param jobId 作业ID
     * @return 作业详情
     */
    JobDetailResponse getJobDetail(Integer jobId);

    /**
     * 提交作业
     *
     * @param jobReq 作业提交请求对象
     * @return 作业 ID
     */
    Integer submitJob(SubmitJobRequest jobReq);
}
