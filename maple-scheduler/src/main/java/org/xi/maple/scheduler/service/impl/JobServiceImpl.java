package org.xi.maple.scheduler.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xi.maple.scheduler.model.Job;
import org.xi.maple.scheduler.service.JobService;

import java.util.List;

/**
 * @author xishihao
 */
@Service
public class JobServiceImpl implements JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    @Override
    public Job getJobById(Integer jobId) {
        return null;
    }

    @Override
    public int updateJobStatus(Integer jobId, String status) {
        return 0;
    }

    @Override
    public List<Job> getEngineRunningJobs(int engineId) {
        return null;
    }
}
