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
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.redis.model.MapleClusterQueue;
import org.xi.maple.redis.model.MapleEngineExecutionQueue;
import org.xi.maple.redis.util.MapleRedisUtil;
import org.xi.maple.scheduler.client.EngineManagerClient;
import org.xi.maple.scheduler.client.PersistenceClient;
import org.xi.maple.scheduler.service.K8sClusterService;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xishihao
 */
@Component
public class ScheduledExecutions implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledExecutions.class);

    final RedissonClient redissonClient;

    final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    final K8sClusterService k8sClusterService;

    final PersistenceClient persistenceClient;

    final EngineManagerClient engineManagerClient;

    final ConcurrentMap<String, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<>();

    public ScheduledExecutions(RedissonClient redissonClient,
                               ThreadPoolTaskExecutor threadPoolTaskExecutor,
                               ThreadPoolTaskScheduler threadPoolTaskScheduler,
                               K8sClusterService k8sClusterService,
                               PersistenceClient persistenceClient,
                               EngineManagerClient engineManagerClient) {
        this.redissonClient = redissonClient;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.k8sClusterService = k8sClusterService;
        this.persistenceClient = persistenceClient;
        this.engineManagerClient = engineManagerClient;
    }

    /**
     * 消费作业
     */
    @Scheduled(fixedDelay = 5000)
    public void consumeJobs() {
        logger.info("开始消费队列...");
        List<EngineExecutionQueue> queueList = persistenceClient.getExecQueueList(new EngineExecutionQueueQueryRequest());
        if (queueList == null || queueList.isEmpty()) {
            logger.warn("队列列表为空");
            return;
        }

        for (EngineExecutionQueue executionQueue : queueList) {
            if (!futureMap.containsKey(executionQueue.getQueueName())) {
                ScheduledFuture<?> scheduledFuture =
                        threadPoolTaskScheduler.scheduleWithFixedDelay(() -> consumeQueueJobs(executionQueue), 5000);
                futureMap.put(executionQueue.getQueueName(), scheduledFuture);
            }
        }
    }

    /**
     * 消费排队中的作业
     *
     * @param executionQueue redis 队列
     */
    private void consumeQueueJobs(EngineExecutionQueue executionQueue) {
        logger.info("正在消费队列作业 <{}> ...", executionQueue.getQueueName());

        RDeque<MapleEngineExecutionQueue.QueueItem> deque = redissonClient.getDeque(executionQueue.getQueueName(), JsonJacksonCodec.INSTANCE);
        RLock lock = redissonClient.getLock(executionQueue.getLockName());

        AtomicBoolean continueRunning = new AtomicBoolean(true);
        while (continueRunning.get()) {
            MapleRedisUtil.tryLockAndExecute(lock, executionQueue.getLockName(), () -> {
                // 消费作业
                MapleEngineExecutionQueue.QueueItem queueItem = deque.poll();
                // 如果队列为空，直接返回
                if (queueItem == null) {
                    logger.info("作业执行队列为空 <{}> ", executionQueue.getQueueName());
                    continueRunning.set(false);
                    return;
                }
                EngineExecutionDetailResponse execution = persistenceClient.getExecutionById(queueItem.getExecId());
                MapleClusterQueue cachedQueueInfo = k8sClusterService.getCachedQueueInfo(executionQueue.getCluster(), executionQueue.getClusterQueue());
                // 单次任务需要新建引擎，判断队列是否有排队任务，有排队任务说明资源不足，直接返回
                if (cachedQueueInfo == null) {
                    logger.error("队列不存在，cluster: {}, queue: {}", executionQueue.getCluster(), executionQueue.getClusterQueue());
                    // todo 修改作业状态
                } else if (cachedQueueInfo.getPendingApps() > 0) {
                    logger.warn("队列没有足够的资源，cluster: {}, queue: {}, 任务重新加回队列", executionQueue.getCluster(), executionQueue.getClusterQueue());
                    deque.addFirst(queueItem);
                    continueRunning.set(false);
                } else {
                    submitExecution(execution);
                }
            });
        }
    }

    private void submitExecution(EngineExecutionDetailResponse execution) {
        logger.info("submit execution: {}", execution);
        // threadPoolTaskExecutor.submit(() -> {
        //     engineManagerClient.execute(execution);
        // });
    }

    public void clearScheduling() {
        logger.info("cancel jobs");
        futureMap.values().forEach(scheduledFuture -> scheduledFuture.cancel(true));
    }

    @Override
    public void run(String... args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(this::clearScheduling));
    }
}