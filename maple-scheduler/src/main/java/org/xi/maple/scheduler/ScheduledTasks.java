package org.xi.maple.scheduler;

import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.xi.maple.common.constant.JobPriorityConstants;
import org.xi.maple.common.constant.JobStatusConstants;
import org.xi.maple.common.constant.JobTypeConstants;
import org.xi.maple.redis.model.MapleClusterQueue;
import org.xi.maple.redis.model.MapleJobQueue;
import org.xi.maple.redis.util.MapleRedisUtil;
import org.xi.maple.scheduler.model.EngineInstance;
import org.xi.maple.scheduler.persistence.entity.JobEntity;
import org.xi.maple.scheduler.service.ClusterQueueService;
import org.xi.maple.scheduler.service.EngineInstanceService;
import org.xi.maple.scheduler.service.JobQueueService;
import org.xi.maple.scheduler.service.JobService;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xishihao
 */
@Component
public class ScheduledTasks implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    final RedissonClient redissonClient;

    final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    final JobService jobService;
    final EngineInstanceService engineInstanceService;
    final ClusterQueueService clusterQueueService;
    final JobQueueService jobQueueService;

    final ConcurrentMap<String, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<>();

    public ScheduledTasks(RedissonClient redissonClient,
                          ThreadPoolTaskExecutor threadPoolTaskExecutor,
                          ThreadPoolTaskScheduler threadPoolTaskScheduler,
                          JobService jobService,
                          EngineInstanceService engineInstanceService,
                          ClusterQueueService clusterQueueService,
                          JobQueueService jobQueueService) {
        this.redissonClient = redissonClient;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.jobService = jobService;
        this.engineInstanceService = engineInstanceService;
        this.clusterQueueService = clusterQueueService;
        this.jobQueueService = jobQueueService;
    }

    public void clearScheduling() {
        System.out.println("cancel jobs");
        futureMap.values().forEach(scheduledFuture -> {
            scheduledFuture.cancel(true);
        });
    }

    /**
     * 消费作业
     */
    @Scheduled(fixedDelay = 5000)
    public void consumeJobs() {
        List<MapleJobQueue> queueList = jobQueueService.getJobQueues();
        if (queueList == null || queueList.isEmpty()) {
            return;
        }

        for (MapleJobQueue jobQueue : queueList) {
            if (!futureMap.containsKey(jobQueue.getQueueName())) {
                threadPoolTaskExecutor.execute(() -> consumeQueueJobs(jobQueue));
            }
        }
    }

    /**
     * 故障引擎的作业转移
     */
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
     * @param jobQueue redis 队列
     */
    private void consumeQueueJobs(MapleJobQueue jobQueue) {
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleWithFixedDelay(() -> {
            RDeque<MapleJobQueue.QueueItem> deque =
                    redissonClient.getDeque(jobQueue.getQueueName(), JsonJacksonCodec.INSTANCE);
            RLock lock = redissonClient.getLock(jobQueue.getLockName());

            AtomicBoolean continueRunning = new AtomicBoolean(true);
            while (!continueRunning.get()) {
                MapleRedisUtil.tryLockAndExecute(lock, jobQueue.getLockName(), () -> {
                    // 消费作业
                    MapleJobQueue.QueueItem queueJobItem = deque.getFirst();
                    if (queueJobItem == null) {
                        continueRunning.set(false);
                        return;
                    }
                    JobEntity job = jobService.getJobById(queueJobItem.getJobId());
                    MapleClusterQueue cachedQueueInfo =
                            clusterQueueService.getCachedQueueInfo(jobQueue.getCluster(), jobQueue.getClusterQueue());
                    if (JobTypeConstants.ONCE.equals(jobQueue.getType()) && cachedQueueInfo.getPendingApps() > 0) {
                        return;
                    }

                    /*
                    1. lock
                    2. 选择引擎，提交任务
                    3. 将引擎置执行任务数 + 1
                    4. deque.poll();
                    5. unlock
                     */
                });
            }
        }, 5000);
        futureMap.put(jobQueue.getQueueName(), scheduledFuture);
    }

    /**
     * 将引擎下的作业推回队列
     *
     * @param engineInstance 引擎实例
     */
    private void pushJobsBackToQueue(EngineInstance engineInstance) {
        List<JobEntity> jobs = jobService.getEngineRunningJobs(engineInstance.getId());
        for (JobEntity job : jobs) {
            int priority = JobPriorityConstants.increasePriority(job.getRunPriority());
            MapleJobQueue jobQueue = MapleRedisUtil.getJobQueue(job.getCluster(), job.getQueue(),
                    job.getFromApp(), job.getJobType(), job.getGroup(), priority);
            RLock lock = redissonClient.getLock(jobQueue.getLockName());
            MapleRedisUtil.waitLockAndExecute(lock, jobQueue.getLockName(), 10, TimeUnit.SECONDS, () -> {
                RDeque<MapleJobQueue.QueueItem> deque =
                        redissonClient.getDeque(jobQueue.getQueueName(), JsonJacksonCodec.INSTANCE);
                // 将作业重新放回队列
                deque.addFirst(new MapleJobQueue.QueueItem(job.getId(), System.currentTimeMillis()));
                // 提高优先级，修改作业状态
                job.setRunPriority(priority);
                job.setStatus(JobStatusConstants.ACCEPTED);
                jobService.updateJobById(job, job.getId());
            });
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(this::clearScheduling));
    }
}