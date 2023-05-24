package org.xi.maple.scheduler.service;

import org.xi.maple.scheduler.persistence.entity.EngineInstanceEntity;
import org.xi.maple.scheduler.persistence.entity.JobEntity;

import java.util.List;

/**
 * @author xishihao
 */
public interface JobService {

    /**
     * 根据作业ID获取作业
     *
     * @param jobId 作业ID
     * @return 作业
     */
    JobEntity getJobById(Integer jobId);

    /**
     * 根据作业ID更新作业
     *
     * @param entity 作业
     * @param jobId  作业ID
     * @return 更新结果
     */
    int updateJobById(JobEntity entity, Integer jobId);

    /**
     * 获取引擎运行中的作业
     *
     * @param engineId 引擎ID
     * @return 作业列表
     */
    List<JobEntity> getEngineRunningJobs(int engineId);

    /**
     * 提交作业到选中的引擎
     *
     * @param job    作业
     * @param engine 引擎
     */
    void submitJobToEngine(JobEntity job, EngineInstanceEntity engine);

    /**
     * 提交作业到新的引擎
     *
     * @param job 作业
     */
    void submitJobToNewEngine(JobEntity job);
}
