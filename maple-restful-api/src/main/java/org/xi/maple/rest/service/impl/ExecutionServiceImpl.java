package org.xi.maple.rest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.xi.maple.rest.client.PersistenceClient;
import org.xi.maple.rest.client.SchedulerClient;
import org.xi.maple.rest.service.MapleAppService;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.util.SecurityUtils;
import org.xi.maple.rest.configuration.properties.MapleSecurityProperties;
import org.xi.maple.rest.service.ExecutionService;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.redis.model.MapleEngineExecutionQueue;
import org.xi.maple.redis.util.MapleRedisUtil;

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
    final MapleSecurityProperties securityProperties;

    @Autowired
    public ExecutionServiceImpl(RedisTemplate<String, Object> redisTemplate, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient, SchedulerClient schedulerClient, MapleAppService mapleAppService, MapleSecurityProperties securityProperties) {
        this.redisTemplate = redisTemplate;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.persistenceClient = persistenceClient;
        this.schedulerClient = schedulerClient;
        this.mapleAppService = mapleAppService;
        this.securityProperties = securityProperties;
    }

    @Override
    public String getExecutionStatus(Integer jobId) {
        EngineExecutionDetailResponse detail = persistenceClient.getExecutionById(jobId);
        if (detail == null) {
            throw new MapleDataNotFoundException(String.format("作业 %s 不存在", jobId));
        }
        return detail.getStatus();
    }

    @Override
    public EngineExecutionDetailResponse detail(Integer id) {
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
    public Integer submit(EngineExecutionAddRequest submitReq, Long timestamp, String secret) {
        checkSecurity(submitReq.getFromApp(), secret, timestamp, submitReq.getExecUniqId(), submitReq.getExecName());
        final Integer id = persistenceClient.addExecution(submitReq);
        if (id == null || id < 0) {
            return id;
        }
        threadPoolTaskExecutor.execute(() -> {
            MapleEngineExecutionQueue execQueue = MapleRedisUtil.getEngineExecutionQueue(submitReq.getCluster(), submitReq.getResourceGroup(),
                    submitReq.getFromApp(), submitReq.getGroup(), submitReq.getPriority());
            persistenceClient.addOrUpdateExecQueue(execQueue);
            logger.info("插入队列：{}, id: {}", execQueue.getQueueName(), id);
            redisTemplate.opsForList().leftPush(execQueue.getQueueName(), new MapleEngineExecutionQueue.QueueItem(id, System.currentTimeMillis()));
            persistenceClient.updateExecutionStatusById(id, new EngineExecutionUpdateStatusRequest(EngineExecutionStatus.ACCEPTED.toString()));
        });
        return id;
    }

    @Override
    public Integer submitNow(EngineExecutionAddRequest submitReq, Long timestamp, String secret) {
        checkSecurity(submitReq.getFromApp(), secret, timestamp, submitReq.getExecUniqId(), submitReq.getExecName());

        final Integer id = persistenceClient.addExecution(submitReq);
        schedulerClient.submitExecution(id);
        return id;
    }

    @Override
    public Object kill(Integer id, Long timestamp, String secret) {
        EngineExecutionDetailResponse detail = detail(id);
        checkSecurity(detail.getFromApp(), secret, timestamp, detail.getExecUniqId(), detail.getExecName());
        return schedulerClient.killExecution(id);
    }

    @Override
    public Object stop(Integer id, Long timestamp, String secret, Map<String, ?> cancelParams) {
        EngineExecutionDetailResponse detail = detail(id);
        checkSecurity(detail.getFromApp(), secret, timestamp, detail.getExecUniqId(), detail.getExecName());
        return schedulerClient.stopExecution(id, cancelParams);
    }

    private void checkSecurity(String fromApp, String secret, Long timestamp, String... fieldValues) {
        if (!Boolean.TRUE.equals(securityProperties.getAppCheck())) {
            return;
        }
        String secretKey = mapleAppService.getAppKey(fromApp);
        SecurityUtils.checkSecurity(secretKey, secret, timestamp, fieldValues);
    }
}
