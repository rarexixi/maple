package org.xi.maple.scheduler.service;

import org.xi.maple.redis.model.MapleJobQueue;
import org.xi.maple.scheduler.persistence.entity.JobEntity;

import java.util.List;

/**
 * @author xishihao
 */
public interface JobQueueService {
    List<MapleJobQueue> getJobQueues();
}
