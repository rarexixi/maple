package org.xi.maple.scheduler;

import io.reactivex.rxjava3.core.Single;
import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonRxClient;
import org.redisson.codec.JsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;
import org.xi.maple.redis.model.ClusterMessage;
import org.xi.maple.redis.model.MapleEngineExecutionQueue;
import org.xi.maple.redis.util.MapleRedisUtil;
import org.xi.maple.scheduler.service.ExecutionService;

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

    final RedissonRxClient redissonRxClient;

    final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    final ExecutionService executionService;

    final ConcurrentMap<String, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<>();

    public ScheduledExecutions(RedissonClient redissonClient, RedissonRxClient redissonRxClient,
                               ThreadPoolTaskExecutor threadPoolTaskExecutor, ThreadPoolTaskScheduler threadPoolTaskScheduler,
                               ExecutionService executionService) {
        this.redissonClient = redissonClient;
        this.redissonRxClient = redissonRxClient;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.executionService = executionService;
    }

    /**
     * 消费作业
     */
    @Scheduled(fixedDelay = 5000)
    public void consumeJobs() {
        logger.info("开始消费队列...");
        List<EngineExecutionQueue> queueList = executionService.getExecQueueList(new EngineExecutionQueueQueryRequest());
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

                EngineExecutionDetailResponse execution = executionService.getExecutionById(queueItem.getExecId());
                if (execution == null) {
                    logger.error("作业不存在，id: {}", queueItem.getExecId());
                    return;
                }
                if (!executionQueue.getCluster().equals(execution.getCluster()) || !executionQueue.getClusterQueue().equals(execution.getClusterQueue())) {
                    logger.info("作业不在当前队列，id: {}, cluster: {}, queue: {}", queueItem.getExecId(), executionQueue.getCluster(), executionQueue.getClusterQueue());
                    executionService.updateExecutionStatus(execution.getId(), EngineExecutionStatus.FAILED);
                    return;
                }
                threadPoolTaskExecutor.submit(() -> executionService.submitExecution(execution, () -> {
                    logger.warn("队列没有足够的资源，cluster: {}, queue: {}, 任务重新加回队列", execution.getCluster(), execution.getClusterQueue());
                    deque.addFirst(queueItem);
                    continueRunning.set(false);
                }));
            });
        }
    }

    public void clearScheduling() {
        logger.info("cancel jobs");
        futureMap.values().forEach(scheduledFuture -> scheduledFuture.cancel(true));
    }

    @Override
    public void run(String... args) throws Exception {
        Single<Integer> test = redissonRxClient.getTopic(ClusterMessage.CLUSTER_CHANNEL).addListener(String.class, (channel, msg) -> {
            logger.info("收到集群消息: {}", msg);
            executionService.refreshCluster(ClusterMessage.getClusterMessage(msg));
        });
        test.subscribe();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            clearScheduling();
        }));
    }
}