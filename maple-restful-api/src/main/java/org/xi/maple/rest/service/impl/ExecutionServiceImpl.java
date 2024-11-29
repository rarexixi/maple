package org.xi.maple.rest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.exception.MapleValidException;
import org.xi.maple.common.model.MapleEngineExecutionQueue;
import org.xi.maple.common.util.MapleRedisUtil;
import org.xi.maple.persistence.model.request.EngineExecutionQueueSaveReq;
import org.xi.maple.persistence.model.request.EngineExecutionSaveReq;
import org.xi.maple.persistence.model.request.EngineExecutionStatusUpdateReq;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;
import org.xi.maple.rest.client.PersistenceClient;
import org.xi.maple.rest.client.SchedulerClient;
import org.xi.maple.rest.service.ExecutionService;
import org.xi.maple.rest.service.MapleAppService;
import org.xi.maple.service.util.ObjectUtils;

import java.util.Map;

/**
 * @author xishihao
 */
@Service
public class ExecutionServiceImpl implements ExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionServiceImpl.class);

    private final RedisTemplate<String, Object> redisTemplate;
    final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    final PersistenceClient persistenceClient;
    final SchedulerClient schedulerClient;
    final MapleAppService mapleAppService;

    @Autowired
    public ExecutionServiceImpl(RedisTemplate<String, Object> redisTemplate, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient, SchedulerClient schedulerClient, MapleAppService mapleAppService) {
        this.redisTemplate = redisTemplate;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.persistenceClient = persistenceClient;
        this.schedulerClient = schedulerClient;
        this.mapleAppService = mapleAppService;
    }

    @Override
    public String getExecutionStatus(Integer jobId) {
        EngineExecutionDetailResp detail = persistenceClient.getExecutionById(jobId);
        if (detail == null) {
            throw new MapleDataNotFoundException(String.format("作业 %s 不存在", jobId));
        }
        return detail.getStatus();
    }

    @Override
    public EngineExecutionDetailResp detail(Integer id) {
        return persistenceClient.getExecutionById(id);
    }

    /**
     * 提交执行
     * 1. 验证请求是否合法
     * 2. 将执行请求插入数据库，返回执行ID
     * 3. 将执行对象（执行ID，执行优先级，时间戳）插入队列
     * 4. 将执行状态更新为排队中
     *
     * @param submitReq 执行提交请求对象
     * @return 执行记录ID
     */
    @Override
    public Integer submit(EngineExecutionSaveReq submitReq) {
        final Integer id = persistenceClient.addExecution(submitReq);
        if (id == null || id < 0) {
            return id;
        }
        threadPoolTaskExecutor.execute(() -> {
            MapleEngineExecutionQueue execQueue = MapleRedisUtil.getEngineExecutionQueue(submitReq.getCluster(), submitReq.getResourceGroup(),
                    submitReq.getFromApp(), submitReq.getGroup(), submitReq.getPriority());
            EngineExecutionQueueSaveReq saveReq = ObjectUtils.copy(execQueue, EngineExecutionQueueSaveReq.class);
            persistenceClient.upsertExecQueue(saveReq);
            logger.info("插入队列：{}, id: {}", execQueue.getQueueName(), id);
            redisTemplate.opsForList().leftPush(execQueue.getQueueName(), new MapleEngineExecutionQueue.QueueItem(id, System.currentTimeMillis()));
            persistenceClient.updateExecutionStatusById(id, new EngineExecutionStatusUpdateReq(EngineExecutionStatus.ACCEPTED.toString()));
        });
        return id;
    }

    @Override
    public Integer submitNow(EngineExecutionSaveReq submitReq) {
        final Integer id = persistenceClient.addExecution(submitReq);
        schedulerClient.submitExecution(id);
        return id;
    }

    @Override
    public Object kill(Integer id, String app) {
        EngineExecutionDetailResp detail = detail(id);
        if (!app.equals(detail.getFromApp())) {
            throw new MapleValidException("任务来源应用不一致");
        }
        return schedulerClient.killExecution(id);
    }

    @Override
    public Object stop(Integer id, Map<String, ?> cancelParams, String app) {
        EngineExecutionDetailResp detail = detail(id);
        if (!app.equals(detail.getFromApp())) {
            throw new MapleValidException("任务来源应用不一致");
        }
        return schedulerClient.stopExecution(id, cancelParams);
    }

}
