package org.xi.maple.scheduler.service.impl;

import org.springframework.stereotype.Service;
import org.xi.maple.common.util.ObjectUtils;
import org.xi.maple.redis.model.MapleJobQueue;
import org.xi.maple.scheduler.persistence.entity.JobQueueEntity;
import org.xi.maple.scheduler.persistence.mapper.JobQueueMapper;
import org.xi.maple.scheduler.service.JobQueueService;

import java.util.List;

/**
 * @author xishihao
 */
@Service
public class JobQueueServiceImpl implements JobQueueService {

    final JobQueueMapper jobQueueMapper;

    public JobQueueServiceImpl(JobQueueMapper jobQueueMapper) {
        this.jobQueueMapper = jobQueueMapper;
    }

    @Override
    public List<MapleJobQueue> getJobQueues() {
        List<JobQueueEntity> list = jobQueueMapper.selectAll();
        return ObjectUtils.copy(list, MapleJobQueue.class);
    }
}
