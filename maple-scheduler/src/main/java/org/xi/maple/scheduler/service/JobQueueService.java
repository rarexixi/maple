package org.xi.maple.scheduler.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.xi.maple.common.model.OperateResult;
import org.xi.maple.redis.model.MapleJobQueue;
import org.xi.maple.scheduler.persistence.entity.JobEntity;

import java.util.List;

/**
 * @author xishihao
 */
public interface JobQueueService {
    List<MapleJobQueue> getJobQueues();

    OperateResult<Integer> addOrUpdate(MapleJobQueue jobQueue);
}
