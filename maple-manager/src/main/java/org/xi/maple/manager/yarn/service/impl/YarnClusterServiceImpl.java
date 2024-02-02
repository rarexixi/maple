package org.xi.maple.manager.yarn.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.constant.DeletedConstant;
import org.xi.maple.common.constant.EngineExecutionStatus;
import org.xi.maple.common.function.ThrowableFunction;
import org.xi.maple.common.util.ActionUtils;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.manager.configuration.properties.MapleManagerProperties;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.request.EngineExecutionUpdateStatusRequest;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.manager.client.PersistenceClient;
import org.xi.maple.manager.constant.MapleConstants;
import org.xi.maple.manager.function.UpdateExecStatusFunc;
import org.xi.maple.manager.model.ClusterQueue;
import org.xi.maple.manager.yarn.model.*;
import org.xi.maple.manager.yarn.service.YarnClusterService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Function;

/**
 * @author xishihao
 */
@Service("yarnClusterService")
public class YarnClusterServiceImpl implements YarnClusterService, CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(YarnClusterServiceImpl.class);

    private final PersistenceClient client;

    private final UpdateExecStatusFunc updateExecStatusFunc;

    private final MapleManagerProperties managerProperties;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private Map<String, ClusterQueue> CLUSTER_QUEUE_MAP;

    private Map<String, ClusterListItemResponse> CLUSTER_MAP;

    public YarnClusterServiceImpl(PersistenceClient client, UpdateExecStatusFunc updateExecStatusFunc, MapleManagerProperties managerProperties, ThreadPoolTaskExecutor threadPoolTaskExecutor, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.client = client;
        this.updateExecStatusFunc = updateExecStatusFunc;
        this.managerProperties = managerProperties;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        CLUSTER_QUEUE_MAP = new ConcurrentHashMap<>();
        CLUSTER_MAP = new ConcurrentHashMap<>();
    }

    // region engine operation

    @Override
    public Object kill(String name, String applicationId) {
        ClusterListItemResponse cluster = CLUSTER_MAP.get(name);
        Function<String, HttpUriRequest> getRequest = master -> {
            String uri = String.format("%s/ws/v1/cluster/apps/%s/state", master, applicationId);
            HttpPut request = new HttpPut(uri);
            StringEntity stringEntity = new StringEntity("{\"state\":\"KILLED\"}", ContentType.APPLICATION_JSON);
            request.setEntity(stringEntity);
            return request;
        };
        return masterExec(cluster, "Kill YARN 任务失败", getRequest, responseBody -> JsonUtils.parseObject(responseBody, YarnApplicationKillResult.class));
    }

    // endregion

    @Override
    public void refreshExecutionStatus(String clusterName, String applicationId) {
        ClusterListItemResponse cluster = CLUSTER_MAP.get(clusterName);
        Function<String, HttpUriRequest> getRequest = master -> {
            String uri = String.format("%s/ws/v1/cluster/apps/%s", master, applicationId);
            HttpGet request = new HttpGet(uri);
            request.addHeader("Content-Type", "application/json");
            return request;
        };
        masterExec(cluster, "获取 YARN 作业信息失败", getRequest, responseBody -> {
            YarnApplication result = JsonUtils.parseObject(responseBody, YarnApplication.class);
            if (result == null || result.getApp() == null) {
                logger.info("YARN 作业信息为空");
                return null;
            }
            updateExecStatus(result.getApp());
            return null;
        });
    }

    @Override
    public void refreshExecutionsStatus(String clusterName, String states, Long startedTimeBegin, Long startedTimeEnd) {
        ClusterListItemResponse cluster = client.getClusterByName(clusterName);
        Function<String, HttpUriRequest> getRequest = master -> {
            String uri = String.format("%s/ws/v1/cluster/apps?applicationTags=%s&states=%s&startedTimeBegin=%d&startedTimeEnd=%d", master, MapleConstants.TAG_EXEC, states, startedTimeBegin, startedTimeEnd);
            HttpGet request = new HttpGet(uri);
            request.addHeader("Content-Type", "application/json");
            return request;
        };
        masterExec(cluster, "获取 YARN 作业信息失败", getRequest, this::refresh);
    }

    @Override
    public ClusterQueue getCachedQueueInfo(String clusterName, String queue) {
        return CLUSTER_QUEUE_MAP.getOrDefault(ClusterQueue.getClusterQueueKey(clusterName, queue), null);
    }

    @Override
    public void removeClusterConfig(String clusterName) {
        CLUSTER_MAP.remove(clusterName);
    }

    @Override
    public void addClusterConfig(ClusterDetailResponse cluster) {
        CLUSTER_MAP.put(cluster.getName(), cluster);
    }

    @Override
    public void refreshAllClusterConfig() {
        ClusterQueryRequest request = new ClusterQueryRequest();
        request.setCategory(ClusterCategoryConstants.YARN);
        request.setDeleted(DeletedConstant.VALID);
        List<ClusterListItemResponse> clusters = client.getClusterList(request);
        for (ClusterListItemResponse cluster : clusters) {
            CLUSTER_MAP.put(cluster.getName(), cluster);
        }
    }

    /**
     * 定时缓存 集群-队列 资源
     */
    public void cacheClusterQueueInfo() {
        logger.info("刷新 YARN 队列资源...");
        final Map<String, ClusterQueue> queueMap = new ConcurrentHashMap<>();
        for (ClusterListItemResponse cluster : CLUSTER_MAP.values()) {
            threadPoolTaskExecutor.execute(() -> {
                Function<String, HttpUriRequest> getRequest = master -> {
                    String uri = String.format("%s/ws/v1/cluster/scheduler", master);
                    HttpGet request = new HttpGet(uri);
                    request.addHeader("Content-Type", "application/json");
                    return request;
                };
                YarnScheduler yarnScheduler = masterExec(cluster, "获取 YARN 队列信息失败", getRequest, responseBody -> JsonUtils.parseObject(responseBody, YarnScheduler.class));
                YarnScheduler.Scheduler scheduler;
                YarnScheduler.SchedulerInfo schedulerInfo;
                YarnScheduler.Queues schedulerQueues;
                List<YarnScheduler.Queue> queues;
                if (yarnScheduler == null
                        || (scheduler = yarnScheduler.getScheduler()) == null
                        || (schedulerInfo = scheduler.getSchedulerInfo()) == null
                        || (schedulerQueues = schedulerInfo.getQueues()) == null
                        || (queues = schedulerQueues.getQueue()) == null
                        || queues.isEmpty()) {
                    return;
                }

                for (YarnScheduler.Queue queue : queues) {
                    String key = ClusterQueue.getClusterQueueKey(cluster.getName(), queue.getQueueName());
                    ClusterQueue value = new YarnClusterQueue(queue.getNumPendingApplications());
                    queueMap.put(key, value);
                }
            });
        }
        CLUSTER_QUEUE_MAP = queueMap;
    }

    /**
     * 刷新引擎执行任务状态
     */
    public void refreshExecStatus() {

        // 获取所有未启动的任务
        Function<String, HttpUriRequest> getRequest = master -> {
            String uri = String.format("%s/ws/v1/cluster/apps?applicationTags=%s&states=NEW,NEW_SAVING,SUBMITTED,ACCEPTED", master, MapleConstants.TAG_EXEC);
            HttpGet request = new HttpGet(uri);
            request.addHeader("Content-Type", "application/json");
            return request;
        };
        // 获取30分钟以内的任务, 30分钟还没启动可判定失败，todo 根据 yarn 超时时间进行判断，监听不能主动上报的任务
        Function<String, HttpUriRequest> getRunningRequest = master -> {
            String uri = String.format("%s/ws/v1/cluster/apps?applicationTags=%s&states=RUNNING&startedTimeBegin=%d", master, MapleConstants.TAG_EXEC, System.currentTimeMillis() - 30 * 60 * 1000);
            HttpGet request = new HttpGet(uri);
            request.addHeader("Content-Type", "application/json");
            return request;
        };
        // 调度周期为5s，获取10s内结束的任务
        Function<String, HttpUriRequest> getFinishedRequest = master -> {
            String uri = String.format("%s/ws/v1/cluster/apps?applicationTags=%s&states=FINISHED,FAILED,KILLED&finishedTimeBegin=%d", master, MapleConstants.TAG_EXEC, System.currentTimeMillis() - 10 * 1000);
            HttpGet request = new HttpGet(uri);
            request.addHeader("Content-Type", "application/json");
            return request;
        };
        for (ClusterListItemResponse cluster : CLUSTER_MAP.values()) {
            masterExec(cluster, "获取 YARN 运行之前的作业信息失败", getRequest, this::refresh);
            masterExec(cluster, "获取 YARN 正在运行中的作业信息失败", getRunningRequest, this::refresh);
            masterExec(cluster, "获取 YARN 结束的作业信息失败", getFinishedRequest, this::refresh);
        }
    }

    private <T> T masterExec(ClusterListItemResponse cluster, String errorMsg, Function<String, HttpUriRequest> getExecRequest, ThrowableFunction<String, T> execResponse) {
        String[] masters = cluster.getAddress().split("[,;]");
        for (String master : masters) {
            HttpUriRequest request = getExecRequest.apply(master);
            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse response = client.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.name());
                    return execResponse.apply(responseBody);
                } else {
                    logger.error("{}: name: {}, master: {}, code: {}", errorMsg, cluster.getName(), master, statusCode);
                }
            } catch (Throwable t) {
                logger.error("{}: name: {}, master: {}", errorMsg, cluster.getName(), master, t);
            }
        }
        return null;
    }

    private Void refresh(String responseBody) throws IOException {
        YarnApplications result = JsonUtils.parseObject(responseBody, YarnApplications.class);
        if (result == null || result.getApps() == null || result.getApps().getApp() == null) {
            logger.info("YARN 作业信息为空");
            return null;
        }
        for (YarnApp app : result.getApps().getApp()) {
            updateExecStatus(app);
        }
        return null;
    }

    private void updateExecStatus(YarnApp app) {
        String[] tags = app.getApplicationTags().split(",");
        for (String tag : tags) {
            if (!tag.startsWith(MapleConstants.TAG_ID_PREFIX)) {
                continue;
            }
            String execIdStr = tag.substring(MapleConstants.TAG_ID_PREFIX_LEN);
            if (StringUtils.isBlank(execIdStr) || !StringUtils.isNumeric(execIdStr)) {
                break;
            }
            Integer execId = Integer.parseInt(execIdStr);
            /*
             * NEW - 应用程序已创建但尚未提交。
             * NEW_SAVING - 应用程序新建完毕，正在保存到资源管理器（ResourceManager）。
             * SUBMITTED - 应用程序已提交，等待调度。
             * ACCEPTED - 应用程序已被资源管理器接受，正在等待资源分配。
             * RUNNING - 应用程序正在运行中。
             * FINISHED - 应用程序已经完成，这是一个最终状态。
             * FAILED - 应用程序运行失败，这是一个最终状态。
             * KILLED - 应用程序被终止或杀死，这是一个最终状态。
             */
            String state = app.getState();
            /*
             * SUCCEEDED - 应用程序成功完成了所有任务并按预期退出。
             * FAILED - 应用程序未能正确完成，出现错误或异常导致任务失败。
             * KILLED - 应用程序由于某种外部干预（例如用户请求或资源管理策略）而被明确地杀死。
             */
            String finalStatus = app.getFinalStatus();
            if ("FINISHED".equals(state)) {
                if ("UNDEFINED".equals(finalStatus)) {
                    state = "RUNNING";
                } else {
                    state = finalStatus;
                }
            } else if ("FAILED".equals(state)) {
                state = EngineExecutionStatus.FAILED.toString();
            } else if ("KILLED".equals(state)) {
                state = EngineExecutionStatus.KILLED.toString();
            } else {
                state = EngineExecutionStatus.RUNNING.toString();
            }
            EngineExecutionUpdateStatusRequest request = new EngineExecutionUpdateStatusRequest(EngineExecutionStatus.valueOf(state).toString(), state);
            updateExecStatusFunc.apply(execId, request);
        }
    }

    private ScheduledFuture<?> clusterConfigScheduledFuture = null;
    private ScheduledFuture<?> clusterQueueRefreshScheduledFuture = null;
    private ScheduledFuture<?> clusterJobScheduledFuture = null;

    /**
     * 开启刷新集群配置（仅配置内容）
     */
    @Override
    public void startRefreshScheduler() {
        clusterConfigScheduledFuture = threadPoolTaskScheduler.scheduleWithFixedDelay(this::refreshAllClusterConfig, managerProperties.getYarnRefreshPeriod());
        clusterQueueRefreshScheduledFuture = threadPoolTaskScheduler.scheduleWithFixedDelay(this::cacheClusterQueueInfo, 5000);
        clusterJobScheduledFuture = threadPoolTaskScheduler.scheduleWithFixedDelay(this::refreshExecStatus, 5000); // todo 如果状态有回调写入，可以考虑加长时间
    }

    /**
     * 关闭刷新集群配置（仅配置内容）
     */
    @Override
    public void stopRefreshScheduler() {
        ActionUtils.cancelScheduledFuture(clusterConfigScheduledFuture);
        ActionUtils.cancelScheduledFuture(clusterQueueRefreshScheduledFuture);
        ActionUtils.cancelScheduledFuture(clusterJobScheduledFuture);
    }

    @Override
    public void run(String... args) throws Exception {
        if (managerProperties.isRefreshYarn()) {
            startRefreshScheduler();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ActionUtils.executeQuietly(() -> stopRefreshScheduler());
        }));
    }
}
