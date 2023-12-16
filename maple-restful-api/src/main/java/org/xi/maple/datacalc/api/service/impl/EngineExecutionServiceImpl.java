package org.xi.maple.datacalc.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.exception.MapleValidException;
import org.xi.maple.common.util.SecurityUtils;
import org.xi.maple.datacalc.api.client.PersistenceClient;
import org.xi.maple.datacalc.api.configuration.MapleSecurityProperties;
import org.xi.maple.datacalc.api.service.EngineExecutionService;
import org.xi.maple.datacalc.api.service.MapleAppService;
import org.xi.maple.persistence.model.request.EngineExecutionAddRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResponse;
import org.xi.maple.redis.model.MapleEngineExecutionQueue;
import org.xi.maple.redis.util.MapleRedisUtil;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author xishihao
 */
@Service
public class EngineExecutionServiceImpl implements EngineExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(EngineExecutionServiceImpl.class);

    final RedissonClient redissonClient;
    final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    final PersistenceClient persistenceClient;
    final MapleAppService mapleAppService;
    final MapleSecurityProperties securityProperties;

    @Autowired
    public EngineExecutionServiceImpl(RedissonClient redissonClient, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient, MapleAppService mapleAppService, MapleSecurityProperties securityProperties) {
        this.redissonClient = redissonClient;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.persistenceClient = persistenceClient;
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
     * 1. 将执行请求插入数据库，返回执行ID
     * 2. 队列加锁
     * 3. 将执行对象（执行ID，执行优先级，时间戳）插入队列
     * 4. 将执行状态更新为排队中
     * 5. 释放锁
     *
     * @param submitReq 执行提交请求对象
     * @return 执行记录ID
     */
    @Override
    public Integer submit(EngineExecutionAddRequest submitReq, String timestamp, String secret) {
        if (Boolean.TRUE.equals(securityProperties.getAppCheck())) {
            if (System.currentTimeMillis() - Long.parseLong(timestamp) > 1000 * 60 * 5) {
                throw new MapleValidException("请求已过期");
            }

            String fromApp = submitReq.getFromApp();
            String secretStr = String.join("#;", new String[]{submitReq.getUniqueId(), submitReq.getExecName(), timestamp});
            String key = mapleAppService.getAppKey(fromApp);
            if (StringUtils.isBlank(key)) {
                throw new MapleValidException("应用不存在/设置不正确");
            }
            try {
                if (!SecurityUtils.valid(key, secretStr, secret)) {
                    throw new MapleValidException("参数验证失败");
                }
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new MapleValidException("参数验证失败", e);
            }
        }
        final Integer id = persistenceClient.addExecution(submitReq);
        threadPoolTaskExecutor.execute(() -> {
            MapleEngineExecutionQueue execQueue = MapleRedisUtil.getEngineExecutionQueue(submitReq.getCluster(), submitReq.getClusterQueue(),
                    submitReq.getFromApp(), submitReq.getGroup(), submitReq.getPriority());
            persistenceClient.addOrUpdateExecQueue(execQueue);
            RLock lock = redissonClient.getLock(execQueue.getLockName());
            MapleRedisUtil.waitLockAndExecute(lock, execQueue.getLockName(), 10, 2, () -> {
                RDeque<MapleEngineExecutionQueue.QueueItem> deque = redissonClient.getDeque(execQueue.getQueueName(), JsonJacksonCodec.INSTANCE);
                deque.addLast(new MapleEngineExecutionQueue.QueueItem(id, System.currentTimeMillis()));
                persistenceClient.updateExecutionStatusById(new EngineExecutionUpdateStatusRequest(id, EngineExecutionStatus.ACCEPTED));
            }, () -> logger.error("Add to queue failed " + submitReq));
        });
        return id;
    }
}
