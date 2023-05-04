package org.xi.maple.scheduler.service;

import org.xi.maple.scheduler.persistence.entity.JobEntity;

import java.util.List;

/**
 * @author xishihao
 */
public interface JobService {

    JobEntity getJobById(Integer jobId);

    int updateJobById(JobEntity entity, Integer jobId);

    List<JobEntity> getEngineRunningJobs(int engineId);
}
