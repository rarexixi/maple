package org.xi.maple.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.model.ClusterMessage;
import org.xi.maple.common.model.MapleEngineExecutionQueue;
import org.xi.maple.common.util.ActionUtils;
import org.xi.maple.manager.configuration.properties.MapleManagerProperties;
import org.xi.maple.manager.service.ExecutionService;
import org.xi.maple.persistence.model.request.EngineExecutionQueueQueryRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.persistence.model.response.EngineExecutionQueue;

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
public class ScheduledExecutions implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledExecutions.class);

    private final ExecutionService executionService;

    private final MapleManagerProperties managerProperties;

    private final RedisTemplate<String, Object> redisTemplate;

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final ConcurrentMap<String, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<>();

    public ScheduledExecutions(ExecutionService executionService, MapleManagerProperties managerProperties,
                               RedisTemplate<String, Object> redisTemplate, RedisMessageListenerContainer redisMessageListenerContainer,
                               ThreadPoolTaskExecutor threadPoolTaskExecutor, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.executionService = executionService;
        this.managerProperties = managerProperties;
        this.redisTemplate = redisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    /**
     * 消费作业
     */
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

        AtomicBoolean continueRunning = new AtomicBoolean(true);
        while (continueRunning.get()) {
            // 消费作业
            MapleEngineExecutionQueue.QueueItem queueItem = (MapleEngineExecutionQueue.QueueItem) redisTemplate.opsForList().rightPop(executionQueue.getQueueName(), 10, TimeUnit.SECONDS);

            // 如果队列为空，直接返回
            if (queueItem == null) {
                logger.info("作业执行队列为空 <{}> ", executionQueue.getQueueName());
                continueRunning.set(false);
                return;
            }
            if (queueItem.getExecId() == null) {
                logger.error("作业ID为空");
                continue;
            }

            EngineExecutionDetailResponse execution = executionService.getExecutionById(queueItem.getExecId());
            if (execution == null) {
                logger.error("作业不存在，id: {}", queueItem.getExecId());
                continue;
            }
            if (!executionQueue.getCluster().equals(execution.getCluster()) || !executionQueue.getClusterQueue().equals(execution.getResourceGroup())) {
                logger.error("作业不在当前队列，id: {}, cluster: {}, queue: {}", queueItem.getExecId(), executionQueue.getCluster(), executionQueue.getClusterQueue());
                executionService.updateExecutionStatus(execution.getId(), new EngineExecutionUpdateStatusRequest(EngineExecutionStatus.FAILED.toString()));
                continue;
            }
            threadPoolTaskExecutor.submit(() -> executionService.submitExecution(execution, () -> {
                logger.warn("队列没有足够的资源，cluster: {}, queue: {}, 任务重新加回队列", execution.getCluster(), execution.getResourceGroup());
                redisTemplate.opsForList().rightPush(executionQueue.getQueueName(), queueItem);
                continueRunning.set(false);
            }));
        }
    }

    private ScheduledFuture<?> consumeJobScheduledFuture = null;

    public void startConsumeJobScheduler() {
        consumeJobScheduledFuture = threadPoolTaskScheduler.scheduleWithFixedDelay(this::consumeJobs, managerProperties.getConsumeJobPeriod());
    }

    public void stopConsumeJobScheduler() {
        ActionUtils.cancelScheduledFuture(consumeJobScheduledFuture);
    }

    public void clearScheduling() {
        logger.info("cancel jobs");
        futureMap.values().forEach(scheduledFuture -> scheduledFuture.cancel(true));
    }

    @Override
    public void run(String... args) throws Exception {
        if (managerProperties.isConsumeJob()) {
            startConsumeJobScheduler();
        }

        final MessageListener messageListener = (message, pattern) -> {
            logger.info("收到集群消息: {}", message);
            ClusterMessage clusterMessage = (ClusterMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
            executionService.refreshCluster(clusterMessage);
        };
        final ChannelTopic topic = new ChannelTopic(ClusterMessage.CLUSTER_CHANNEL);
        redisMessageListenerContainer.addMessageListener(messageListener, topic);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ActionUtils.executeQuietly(() -> stopConsumeJobScheduler());
            ActionUtils.executeQuietly(() -> clearScheduling());
            ActionUtils.executeQuietly(() -> redisMessageListenerContainer.removeMessageListener(messageListener, topic));
        }));
    }
}