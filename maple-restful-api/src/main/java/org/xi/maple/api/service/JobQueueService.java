package org.xi.maple.api.service;

import org.xi.maple.common.model.OperateResult;
import org.xi.maple.redis.model.MapleJobQueue;

/**
 * @author xishihao
 */
public interface JobQueueService {

    /**
     * 新增或者更新作业队列
     *
     * @param jobQueue 作业队列
     * @return 作业队列结果
     */
    OperateResult<Integer> addOrUpdate(MapleJobQueue jobQueue);
}
