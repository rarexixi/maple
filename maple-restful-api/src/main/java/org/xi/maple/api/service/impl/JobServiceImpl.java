package org.xi.maple.api.service.impl;

import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.xi.maple.api.model.request.SubmitJobRequest;
import org.xi.maple.api.service.JobService;
import org.xi.maple.redis.model.QueueJobItem;
import org.xi.maple.redis.util.MapleRedisUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author xishihao
 */
@Service
public class JobServiceImpl implements JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    final RedissonClient redissonClient;
    final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public JobServiceImpl(RedissonClient redissonClient, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.redissonClient = redissonClient;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    /**
     * 提交作业
     * 1. 将作业插入数据库，返回作业ID
     * 2. 队列加锁
     * 3. 将作业对象（作业ID，作业优先级，时间戳）插入队列
     * 4. 将作业状态更新为排队中
     * 5. 释放锁
     *
     * @param jobReq 作业提交请求对象
     * @return 作业 ID
     */
    @Override
    public Integer submitJob(SubmitJobRequest jobReq) {
        final Integer id = addJob(jobReq);
        threadPoolTaskExecutor.execute(() -> {
            String lockName = MapleRedisUtil.getJobQueueLock(jobReq.getFromApp(), jobReq.getGroup(), jobReq.getJobType(), jobReq.getQueue(), jobReq.getPriority());
            RLock lock = redissonClient.getLock(lockName);

            MapleRedisUtil.waitLockAndExecute(lock, lockName, 10, TimeUnit.SECONDS, () -> {
                String queueName = MapleRedisUtil.getJobQueue(jobReq.getFromApp(), jobReq.getGroup(), jobReq.getJobType(), jobReq.getQueue(), jobReq.getPriority());
                RDeque<QueueJobItem> deque = redissonClient.getDeque(queueName);
                deque.addLast(new QueueJobItem(id, System.currentTimeMillis()));
            }, () -> logger.error("Add to queue failed" + jobReq));
        });
        return id;
    }

    /**
     * 向数据库插入作业
     *
     * @param submitJobRequest 作业提交请求对象
     * @return 作业 ID
     */

    private Integer addJob(SubmitJobRequest submitJobRequest) {
        return 0;
    }
}
