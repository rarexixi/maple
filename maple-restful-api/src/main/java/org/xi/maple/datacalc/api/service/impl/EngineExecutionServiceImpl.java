package org.xi.maple.datacalc.api.service.impl;

import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.xi.maple.common.constant.JobStatusConstants;
import org.xi.maple.datacalc.api.client.EngineExecutionClient;
import org.xi.maple.datacalc.api.client.EngineExecutionQueueClient;
import org.xi.maple.datacalc.api.service.EngineExecutionService;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.redis.model.MapleEngineExecutionQueue;
import org.xi.maple.redis.util.MapleRedisUtil;

/**
 * @author xishihao
 */
@Service
public class EngineExecutionServiceImpl implements EngineExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(EngineExecutionServiceImpl.class);

    final RedissonClient redissonClient;
    final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    final EngineExecutionClient engineExecutionClient;
    final EngineExecutionQueueClient executionQueueClient;

    @Autowired
    public EngineExecutionServiceImpl(RedissonClient redissonClient, ThreadPoolTaskExecutor threadPoolTaskExecutor, EngineExecutionClient engineExecutionClient, EngineExecutionQueueClient executionQueueClient) {
        this.redissonClient = redissonClient;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.engineExecutionClient = engineExecutionClient;
        this.executionQueueClient = executionQueueClient;
    }

    @Override
    public String getExecutionStatus(Integer jobId) {
        EngineExecutionDetailResponse detail = engineExecutionClient.getById(jobId);
        if (detail == null) {
            throw new RuntimeException("");
        }
        return detail.getStatus();
    }

    @Override
    public EngineExecutionDetailResponse detail(Integer id) {
        return engineExecutionClient.getById(id);
    }

    /**
     * 提交执行
     * 1. 将执行请求插入数据库，返回执行ID
     * 2. 队列加锁
     * 3. 将执行对象（执行ID，执行优先级，时间戳）插入队列
     * 4. 将执行状态更新为排队中
     * 5. 释放锁
     *
     * @param submitReq 执行提交请求对象
     * @return 执行记录ID
     */
    @Override
    public Integer submit(EngineExecutionAddRequest submitReq) {
        final Integer id = engineExecutionClient.add(submitReq);
        threadPoolTaskExecutor.execute(() -> {
            MapleEngineExecutionQueue execQueue = MapleRedisUtil.getEngineExecutionQueue(submitReq.getCluster(), submitReq.getClusterQueue(),
                    submitReq.getFromApp(), submitReq.getGroup(), submitReq.getPriority());
            executionQueueClient.addOrUpdate(execQueue);
            RLock lock = redissonClient.getLock(execQueue.getLockName());
            MapleRedisUtil.waitLockAndExecute(lock, execQueue.getLockName(), 10, 2, () -> {
                RDeque<MapleEngineExecutionQueue.QueueItem> deque = redissonClient.getDeque(execQueue.getQueueName(), JsonJacksonCodec.INSTANCE);
                deque.addLast(new MapleEngineExecutionQueue.QueueItem(id, System.currentTimeMillis()));
                engineExecutionClient.updateStatusById(new EngineExecutionUpdateStatusRequest(id, JobStatusConstants.ACCEPTED));
            }, () -> logger.error("Add to queue failed " + submitReq));
        });
        return id;
    }
}
