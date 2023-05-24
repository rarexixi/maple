package org.xi.maple.scheduler.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xi.maple.common.model.OperateResult;
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

    @Cacheable(cacheNames = {"maple"}, key = "'queue'")
    @Override
    public List<MapleJobQueue> getJobQueues() {
        List<JobQueueEntity> list = jobQueueMapper.selectAll();
        return ObjectUtils.copy(list, MapleJobQueue.class);
    }

    @CacheEvict(cacheNames = {"maple"}, key = "'queue'", condition = "#result.type == T(org.xi.maple.common.constant.OperateResultType).NEW")
    @Transactional
    @Override
    public OperateResult<Integer> addOrUpdate(MapleJobQueue jobQueue) {
        JobQueueEntity entity = new JobQueueEntity();
        BeanUtils.copyProperties(jobQueue, entity);
        if (jobQueueMapper.detailByPk(jobQueue.getQueueName()) == null) {
            return OperateResult.newResult(jobQueueMapper.insert(entity));
        } else {
            return OperateResult.updateResult(jobQueueMapper.updateByPk(entity, jobQueue.getQueueName()));
        }
    }
}
