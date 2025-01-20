package org.xi.maple.rest.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.exception.MapleDataNotFoundException;
import org.xi.maple.common.exception.MapleException;
import org.xi.maple.common.exception.MapleValidException;
import org.xi.maple.common.model.MapleEngineExecutionQueue;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.common.util.MapleRedisUtil;
import org.xi.maple.persistence.model.request.EngineExecutionQueueSaveReq;
import org.xi.maple.persistence.model.request.EngineExecutionSaveReq;
import org.xi.maple.persistence.model.request.EngineExecutionStatusUpdateReq;
import org.xi.maple.persistence.model.response.EngineExecutionDetailResp;
import org.xi.maple.persistence.model.response.JobDetailResp;
import org.xi.maple.rest.client.PersistenceClient;
import org.xi.maple.rest.client.ManagerClient;
import org.xi.maple.rest.model.request.ExecReq;
import org.xi.maple.rest.model.request.JobExecReq;
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
    final ManagerClient managerClient;
    final MapleAppService mapleAppService;

    @Autowired
    public ExecutionServiceImpl(RedisTemplate<String, Object> redisTemplate, ThreadPoolTaskExecutor threadPoolTaskExecutor, PersistenceClient persistenceClient, ManagerClient managerClient, MapleAppService mapleAppService) {
        this.redisTemplate = redisTemplate;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.persistenceClient = persistenceClient;
        this.managerClient = managerClient;
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
     * @param execReq 执行提交请求对象
     * @return 执行记录ID
     */
    @Override
    public Integer submit(ExecReq execReq) {
        EngineExecutionSaveReq saveReq = getExecutionSaveReq(execReq);
        return submit(saveReq);
    }

    /**
     * 提交配置的作业
     * 1. 验证请求是否合法
     * 2. 将执行请求插入数据库，返回执行ID
     * 3. 将执行对象（执行ID，执行优先级，时间戳）插入队列
     * 4. 将执行状态更新为排队中
     *
     * @param jobExecReq 执行提交请求对象
     * @return 执行记录ID
     */
    @Override
    public Integer submitJob(JobExecReq jobExecReq) {
        EngineExecutionSaveReq saveReq = getExecutionSaveReq(jobExecReq);
        return submit(saveReq);
    }

    private Integer submit(EngineExecutionSaveReq saveReq) {
        final Integer id = persistenceClient.addExecution(saveReq);
        if (id == null || id < 0) {
            return id;
        }
        EngineExecutionDetailResp execution = persistenceClient.getExecutionById(id);
        threadPoolTaskExecutor.execute(() -> {
            String resourceGroup = getResourceGroup(execution.getClusterCategory(), saveReq.getRunConf());
            MapleEngineExecutionQueue execQueue = MapleRedisUtil.getEngineExecutionQueue(execution.getClusterId(), resourceGroup,
                    saveReq.getFromApp(), saveReq.getUserGroup(), saveReq.getPriority());
            EngineExecutionQueueSaveReq queueSaveReq = ObjectUtils.copy(execQueue, EngineExecutionQueueSaveReq.class);
            persistenceClient.upsertExecQueue(queueSaveReq);
            logger.info("插入队列：{}, id: {}", execQueue.getQueueName(), id);
            redisTemplate.opsForList().leftPush(execQueue.getQueueName(), new MapleEngineExecutionQueue.QueueItem(id, System.currentTimeMillis()));
            persistenceClient.updateExecutionStatusById(id, new EngineExecutionStatusUpdateReq(EngineExecutionStatus.ACCEPTED.toString()));
        });
        return id;
    }

    private EngineExecutionSaveReq getExecutionSaveReq(ExecReq execReq ) {
        return ObjectUtils.copy(execReq, EngineExecutionSaveReq.class);
    }

    private EngineExecutionSaveReq getExecutionSaveReq(JobExecReq jobExecReq) {
        JobDetailResp job = persistenceClient.getJobById(jobExecReq.getJobId());
        EngineExecutionSaveReq saveReq = ObjectUtils.copy(job, EngineExecutionSaveReq.class, "id");
        ObjectUtils.copy(jobExecReq, saveReq);
        saveReq.setExecConf(job.getJobConf());
        return saveReq;
    }

    private String getResourceGroup(String clusterCategory, String runConf) {
        Map<String, Object> runConfMap = JsonUtils.parseObject(runConf, Map.class, null);
        if (runConfMap == null) {
            return null;
        }
        switch (clusterCategory.toUpperCase()) {
            case "YARN":
            case "K8S":
                return (String) runConfMap.get("queue");
            default:
                throw new MapleException("unknown cluster category: " + clusterCategory);
        }
    }

    @Override
    public Integer exec(ExecReq execReq) {
        EngineExecutionSaveReq saveReq = getExecutionSaveReq(execReq);
        final Integer id = persistenceClient.addExecution(saveReq);
        managerClient.submitExecution(id);
        return id;
    }

    @Override
    public Integer execJob(JobExecReq jobExecReq) {
        EngineExecutionSaveReq saveReq = getExecutionSaveReq(jobExecReq);
        final Integer id = persistenceClient.addExecution(saveReq);
        managerClient.submitExecution(id);
        return id;
    }

    @Override
    public Object kill(Integer id, String app) {
        EngineExecutionDetailResp detail = detail(id);
        if (!app.equals(detail.getFromApp())) {
            throw new MapleValidException("任务来源应用不一致");
        }
        return managerClient.killExecution(id);
    }

    @Override
    public Object stop(Integer id, Map<String, ?> cancelParams, String app) {
        EngineExecutionDetailResp detail = detail(id);
        if (!app.equals(detail.getFromApp())) {
            throw new MapleValidException("任务来源应用不一致");
        }
        return managerClient.stopExecution(id, cancelParams);
    }

}
