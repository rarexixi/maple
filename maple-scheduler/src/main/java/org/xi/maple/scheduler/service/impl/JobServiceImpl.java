package org.xi.maple.scheduler.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xi.maple.scheduler.persistence.entity.JobEntity;
import org.xi.maple.scheduler.persistence.mapper.JobMapper;
import org.xi.maple.scheduler.service.JobService;

import java.util.List;

/**
 * @author xishihao
 */
@Service
public class JobServiceImpl implements JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    private final JobMapper jobMapper;

    public JobServiceImpl(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    @Override
    public JobEntity getJobById(Integer jobId) {
        return jobMapper.detailById(jobId);
    }

    @Override
    public int updateJobById(JobEntity jobEntity, Integer jobId) {
        return jobMapper.updateById(jobEntity, jobId);
    }

    @Override
    public List<JobEntity> getEngineRunningJobs(int engineId) {
        return null;
    }
}
