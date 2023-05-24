package org.xi.maple.api.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xi.maple.api.persistence.entity.JobQueueEntity;
import org.xi.maple.api.persistence.mapper.JobQueueMapper;
import org.xi.maple.api.service.JobQueueService;
import org.xi.maple.common.model.OperateResult;
import org.xi.maple.redis.model.MapleJobQueue;

/**
 * @author xishihao
 */
@Service
public class JobQueueServiceImpl implements JobQueueService {

    final JobQueueMapper jobQueueMapper;

    public JobQueueServiceImpl(JobQueueMapper jobQueueMapper) {
        this.jobQueueMapper = jobQueueMapper;
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
