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
import org.xi.maple.common.util.ActionUtils;
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
import java.util.concurrent.*;
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


    /**
     * 故障引擎的作业转移
     */
    @Scheduled(fixedDelay = 5000)
    public void restoreProblematicEngineJobs() {
        // 获取故障引擎
        List<EngineInstance> instances = engineInstanceService.getProblematicEngines();
        CountDownLatch latch = new CountDownLatch(instances.size());
        for (EngineInstance instance : instances) {
            threadPoolTaskExecutor.execute(() -> {
                try {
                    String lockName = MapleRedisUtil.getEngineInstanceLock(instance.getId());
                    MapleRedisUtil.tryLockAndExecute(redissonClient.getLock(lockName), lockName, () -> {
                        // 将作业推回队列
                        pushJobsBackToQueue(instance);
                        // 将引擎设置为问题处理完成
                        engineInstanceService.finishCleaningJobs(instance.getId());
                    });
                } catch (Throwable t) {
                    logger.error("restore problematic engine jobs error" + instance.getId(), t);
                } finally {
                    latch.countDown();
                }
            });
        }
        ActionUtils.executeQuietly(latch::await);
    }

    /**
     * 将引擎下的作业推回队列
     *
     * @param engineInstance 引擎实例
     */
    private void pushJobsBackToQueue(EngineInstance engineInstance) {
        // 获取故障引擎下正在运行的作业
        List<JobEntity> jobs = jobService.getEngineRunningJobs(engineInstance.getId());
        for (JobEntity job : jobs) {
            // 提升故障作业级别
            int priority = JobPriorityConstants.increasePriority(job.getRunPriority());
            MapleJobQueue jobQueue = MapleRedisUtil.getJobQueue(job.getCluster(), job.getQueue(),
                    job.getFromApp(), job.getJobType(), job.getGroup(), priority);
            RLock lock = redissonClient.getLock(jobQueue.getLockName());
            MapleRedisUtil.waitLockAndExecute(lock, jobQueue.getLockName(), 10, 2, () -> {
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

    /**
     * 消费作业
     */
    public void startConsumeJobsScheduler() {
        List<MapleJobQueue> queueList = jobQueueService.getJobQueues();
        if (queueList == null || queueList.isEmpty()) {
            return;
        }

        for (MapleJobQueue jobQueue : queueList) {
            if (!futureMap.containsKey(jobQueue.getQueueName())) {
                threadPoolTaskScheduler.scheduleWithFixedDelay(() -> {
                    logger.info("consume queue jobs: {}", jobQueue.getQueueName());
                    consumeQueueJobs(jobQueue);
                }, 5000);
            }
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
            while (continueRunning.get()) {
                MapleRedisUtil.tryLockAndExecute(lock, jobQueue.getLockName(), () -> {
                    // 消费作业
                    MapleJobQueue.QueueItem queueJobItem = deque.poll();
                    // 如果队列为空，直接返回
                    if (queueJobItem == null) {
                        continueRunning.set(false);
                        return;
                    }
                    JobEntity job = jobService.getJobById(queueJobItem.getJobId());
                    MapleClusterQueue cachedQueueInfo =
                            clusterQueueService.getCachedQueueInfo(jobQueue.getCluster(), jobQueue.getClusterQueue());
                    if (JobTypeConstants.ONCE.equals(jobQueue.getType())) {
                        // 单次任务需要新建引擎，判断队列是否有排队任务，有排队任务直接返回
                        if (cachedQueueInfo.getPendingApps() > 0) {
                            deque.addFirst(queueJobItem);
                            continueRunning.set(false);
                        } else {
                            jobService.submitJobToNewEngine(job);
                        }
                    } else {
                        // 获取当前用户可以提交的引擎锁
                        String engineLockName = MapleRedisUtil.getEngineLock(job.getGroup(), jobQueue.getCluster(),
                                job.getEngineCategory(), jobQueue.getType(), job.getEngineVersion());
                        RLock engineLock = redissonClient.getLock(engineLockName);

                        MapleRedisUtil.waitLockAndExecute(engineLock, engineLockName, 10, 2, () -> {
                            EngineInstance engine =
                                    engineInstanceService.getFreeEngine(job.getCluster(), job.getQueue(),
                                            job.getEngineCategory(), job.getEngineVersion(), job.getGroup());

                            if (engine == null) {
                                if (cachedQueueInfo.getPendingApps() > 0) {
                                    deque.addFirst(queueJobItem);
                                    continueRunning.set(false);
                                } else {
                                    jobService.submitJobToNewEngine(job);
                                }
                            } else {
                                jobService.submitJobToEngine(job, engine);
                            }
                        });
                    }
                });
            }
        }, 5000);
        futureMap.put(jobQueue.getQueueName(), scheduledFuture);
    }

    public void clearScheduling() {
        logger.info("cancel jobs");
        futureMap.values().forEach(scheduledFuture -> scheduledFuture.cancel(true));
    }

    @Override
    public void run(String... args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(this::clearScheduling));
        startConsumeJobsScheduler();
    }
}