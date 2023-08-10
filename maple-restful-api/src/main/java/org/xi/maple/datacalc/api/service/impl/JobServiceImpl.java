package org.xi.maple.datacalc.api.service.impl;

import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.xi.maple.datacalc.api.model.request.SubmitJobRequest;
import org.xi.maple.datacalc.api.model.response.JobDetailResponse;
import org.xi.maple.datacalc.api.persistence.entity.JobEntity;
import org.xi.maple.datacalc.api.persistence.mapper.JobMapper;
import org.xi.maple.datacalc.api.service.JobQueueService;
import org.xi.maple.datacalc.api.service.JobService;
import org.xi.maple.common.constant.EngineTypeConstants;
import org.xi.maple.common.constant.JobStatusConstants;
import org.xi.maple.redis.model.MapleJobQueue;
import org.xi.maple.redis.util.MapleRedisUtil;

/**
 * @author xishihao
 */
@Service
public class JobServiceImpl implements JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    final RedissonClient redissonClient;
    final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    final JobMapper jobMapper;
    final JobQueueService jobQueueService;

    @Autowired
    public JobServiceImpl(RedissonClient redissonClient, ThreadPoolTaskExecutor threadPoolTaskExecutor, JobMapper jobMapper, JobQueueService jobQueueService) {
        this.redissonClient = redissonClient;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.jobMapper = jobMapper;
        this.jobQueueService = jobQueueService;
    }

    /**
     * 获取作业状态
     *
     * @param jobId 作业ID
     * @return 作业状态
     */
    @Override
    public String getJobStatus(Integer jobId) {
        JobEntity entity = jobMapper.detailById(jobId);
        if (entity == null) {
            throw new RuntimeException("");
        }
        return entity.getStatus();
    }

    /**
     * 获取作业详情
     *
     * @param jobId 作业ID
     * @return 作业详情
     */
    @Override
    public JobDetailResponse getJobDetail(Integer jobId) {
        JobEntity entity = jobMapper.detailById(jobId);
        JobDetailResponse detail = new JobDetailResponse();
        BeanUtils.copyProperties(entity, detail);
        return detail;
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
        final Integer jobId = addJob(jobReq);
        threadPoolTaskExecutor.execute(() -> {
            MapleJobQueue jobQueue = MapleRedisUtil.getJobQueue(jobReq.getCluster(), jobReq.getClusterQueue(),
                    jobReq.getEngineCategory(), jobReq.getEngineVersion(),
                    jobReq.getFromApp(), jobReq.getGroup(), jobReq.getPriority());
            jobQueueService.addOrUpdate(jobQueue);
            RLock lock = redissonClient.getLock(jobQueue.getLockName());
            MapleRedisUtil.waitLockAndExecute(lock, jobQueue.getLockName(), 10, 2, () -> {
                RDeque<MapleJobQueue.QueueItem> deque = redissonClient.getDeque(jobQueue.getQueueName(), JsonJacksonCodec.INSTANCE);
                deque.addLast(new MapleJobQueue.QueueItem(jobId, System.currentTimeMillis()));
                updateJobStatus(jobId, JobStatusConstants.ACCEPTED);
            }, () -> logger.error("Add to queue failed " + jobReq));
        });
        return jobId;
    }

    /**
     * 向数据库插入作业
     *
     * @param submitJobRequest 作业提交请求对象
     * @return 作业 ID
     */
    private Integer addJob(SubmitJobRequest submitJobRequest) {
        JobEntity jobEntity = new JobEntity();
        BeanUtils.copyProperties(submitJobRequest, jobEntity);
        jobEntity.setStatus(JobStatusConstants.SUBMITTED);

        int count = jobMapper.insert(jobEntity);
        if (count < 1) {
            throw new RuntimeException("");
        }
        return jobEntity.getId();
    }

    /**
     * 修改作业状态
     *
     * @param jobId     作业ID
     * @param jobStatus 作业状态
     * @return 作业 ID
     */
    private void updateJobStatus(int jobId, String jobStatus) {
        JobEntity jobEntity = new JobEntity();
        jobEntity.setStatus(jobStatus);
        int count = jobMapper.updateById(jobEntity, jobId);
        if (count < 1) {
            throw new RuntimeException("");
        }
    }
}
