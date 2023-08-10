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
import org.xi.maple.scheduler.client.EngineExecutionClient;
import org.xi.maple.scheduler.client.EngineExecutionQueueClient;
import org.xi.maple.scheduler.service.ClusterQueueService;

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

    final ClusterQueueService clusterQueueService;

    final EngineExecutionClient engineExecutionClient;

    final EngineExecutionQueueClient engineExecutionQueueClient;

    final ConcurrentMap<String, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<>();

    public ScheduledExecutions(RedissonClient redissonClient,
                               ThreadPoolTaskExecutor threadPoolTaskExecutor,
                               ThreadPoolTaskScheduler threadPoolTaskScheduler,
                               ClusterQueueService clusterQueueService,
                               EngineExecutionClient engineExecutionClient,
                               EngineExecutionQueueClient engineExecutionQueueClient) {
        this.redissonClient = redissonClient;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.clusterQueueService = clusterQueueService;
        this.engineExecutionClient = engineExecutionClient;
        this.engineExecutionQueueClient = engineExecutionQueueClient;
    }

    /**
     * 消费作业
     */
    @Scheduled(fixedDelay = 5000)
    public void consumeJobs() {
        logger.info("Start to consume jobs...");
        List<EngineExecutionQueue> queueList = engineExecutionQueueClient.getList(new EngineExecutionQueueQueryRequest());
        if (queueList == null || queueList.isEmpty()) {
            return;
        }

        for (EngineExecutionQueue executionQueue : queueList) {
            if (!futureMap.containsKey(executionQueue.getQueueName())) {
                ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleWithFixedDelay(() -> {
                    logger.info("Consuming {} jobs...", executionQueue.getQueueName());
                    consumeQueueJobs(executionQueue);
                }, 5000);
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

        RDeque<MapleEngineExecutionQueue.QueueItem> deque = redissonClient.getDeque(executionQueue.getQueueName(), JsonJacksonCodec.INSTANCE);
        RLock lock = redissonClient.getLock(executionQueue.getLockName());

        AtomicBoolean continueRunning = new AtomicBoolean(true);
        while (continueRunning.get()) {
            MapleRedisUtil.tryLockAndExecute(lock, executionQueue.getLockName(), () -> {
                // 消费作业
                MapleEngineExecutionQueue.QueueItem queueItem = deque.poll();
                // 如果队列为空，直接返回
                if (queueItem == null) {
                    continueRunning.set(false);
                    return;
                }
                EngineExecutionDetailResponse execution = engineExecutionClient.getById(queueItem.getExecId());
                MapleClusterQueue cachedQueueInfo = clusterQueueService.getCachedQueueInfo(executionQueue.getCluster(), executionQueue.getClusterQueue());
                // 单次任务需要新建引擎，判断队列是否有排队任务，有排队任务说明资源不足，直接返回
                if (cachedQueueInfo.getPendingApps() > 0) {
                    deque.addFirst(queueItem);
                    continueRunning.set(false);
                } else {
                    submitExecution(execution);
                }
            });
        }
    }

    private void submitExecution(EngineExecutionDetailResponse execution) {
        // todo
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