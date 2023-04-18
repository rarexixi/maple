package org.xi.maple.scheduler.service;

import org.xi.maple.scheduler.model.Job;

import java.util.List;

/**
 * @author xishihao
 */
public interface JobService {

    Job getJobById(Integer jobId);

    int updateJobStatus(Integer jobId, String status);

    List<Job> getEngineRunningJobs(int engineId);
}
