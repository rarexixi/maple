package org.xi.maple.scheduler;

import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.xi.maple.common.constant.JobStatusConstants;
import org.xi.maple.redis.model.QueueJobItem;
import org.xi.maple.redis.util.MapleRedisUtil;
import org.xi.maple.scheduler.model.EngineInstance;
import org.xi.maple.scheduler.model.Job;
import org.xi.maple.scheduler.model.MapleRedisQueue;
import org.xi.maple.scheduler.service.EngineInstanceService;
import org.xi.maple.scheduler.service.JobService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    final RedissonClient redissonClient;

    final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    final JobService jobService;

    final EngineInstanceService engineInstanceService;

    final ConcurrentMap<String, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<>();

    @Autowired
    public ScheduledTasks(RedissonClient redissonClient, ThreadPoolTaskExecutor threadPoolTaskExecutor, ThreadPoolTaskScheduler threadPoolTaskScheduler, JobService jobService, EngineInstanceService engineInstanceService) {
        this.redissonClient = redissonClient;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.jobService = jobService;
        this.engineInstanceService = engineInstanceService;
    }

    @Scheduled(fixedDelay = 5000)
    public void consumeJobs() {
        List<MapleRedisQueue> queueList = getQueueAndLockList();
        if (queueList == null || queueList.isEmpty()) {
            return;
        }

        for (MapleRedisQueue mapleRedisQueue : queueList) {
            if (!futureMap.containsKey(mapleRedisQueue.getQueueName())) {
                threadPoolTaskExecutor.execute(() -> consumeQueueJobs(mapleRedisQueue));
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void transferProblematicEngineJobs() {
        List<EngineInstance> instances = engineInstanceService.getProblematicEngines();
        for (EngineInstance instance : instances) {
            threadPoolTaskExecutor.execute(() -> {
                String lockName = MapleRedisUtil.getEngineInstanceLock(instance.getId());
                MapleRedisUtil.tryLockAndExecute(redissonClient.getLock(lockName), lockName, () -> {
                    // 将作业推回队列
                    pushJobsBackToQueue(instance);
                    // 将引擎设置为问题处理完成
                    engineInstanceService.finishCleaningJobs(instance.getId());
                });
            });
        }
    }

    /**
     * 消费排队中的作业
     *
     * @param mapleRedisQueue redis 队列
     */
    private void consumeQueueJobs(MapleRedisQueue mapleRedisQueue) {
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleWithFixedDelay(() -> {
            RDeque<QueueJobItem> deque = redissonClient.getDeque(mapleRedisQueue.getQueueName(), JsonJacksonCodec.INSTANCE);
            RLock lock = redissonClient.getLock(mapleRedisQueue.getLockName());

            AtomicBoolean continueRunning = new AtomicBoolean(true);
            while (!continueRunning.get()) {
                MapleRedisUtil.tryLockAndExecute(lock, mapleRedisQueue.getLockName(), () -> {
                    // todo 这里可以多消费几个
                    QueueJobItem queueJobItem = deque.poll();
                    if (queueJobItem == null) {
                        continueRunning.set(false);
                        return;
                    }
                    Job job = jobService.getJobById(queueJobItem.getJobId());
                    // todo 消费作业
                    /*
                    1. lock
                    2. 选择引擎，提交任务
                    3. 将引擎置为busy
                    4. unlock
                     */
                });
            }
        }, 5000);
        futureMap.put(mapleRedisQueue.getQueueName(), scheduledFuture);
    }

    private void pushJobsBackToQueue(EngineInstance engineInstance) {
        List<Job> jobs = jobService.getEngineRunningJobs(engineInstance.getId());
        for (Job job : jobs) {
            String queueName = MapleRedisUtil.getJobQueue(job.getFromApp(), job.getGroup(), job.getJobType(), job.getQueue(), job.getPriority());
            String jobQueueLockName = MapleRedisUtil.getJobQueueLock(job.getFromApp(), job.getGroup(), job.getJobType(), job.getQueue(), job.getPriority());
            MapleRedisUtil.waitLockAndExecute(redissonClient.getLock(jobQueueLockName), jobQueueLockName, 10, TimeUnit.SECONDS, () -> {
                RDeque<QueueJobItem> deque = redissonClient.getDeque(queueName, JsonJacksonCodec.INSTANCE);
                deque.addFirst(new QueueJobItem(job.getId(), System.currentTimeMillis()));
                jobService.updateJobStatus(job.getId(), JobStatusConstants.ACCEPTED);
            });
        }
    }

    private List<MapleRedisQueue> getQueueAndLockList() {
        return null;
    }
}