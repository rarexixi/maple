package org.xi.maple.scheduler.yarn.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xi.maple.common.constant.ClusterCategoryConstants;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.response.ClusterDetailResponse;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.scheduler.client.PersistenceClient;
import org.xi.maple.scheduler.constant.MapleConstants;
import org.xi.maple.scheduler.function.UpdateExecStatusFunc;
import org.xi.maple.scheduler.model.ClusterQueue;
import org.xi.maple.scheduler.yarn.model.YarnApplications;
import org.xi.maple.scheduler.yarn.model.YarnClusterQueue;
import org.xi.maple.scheduler.yarn.model.YarnScheduler;
import org.xi.maple.scheduler.yarn.service.YarnClusterService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xishihao
 */
@Service("yarnClusterService")
public class YarnClusterServiceImpl implements YarnClusterService {

    private static final Logger logger = LoggerFactory.getLogger(YarnClusterServiceImpl.class);

    private final PersistenceClient client;

    private final UpdateExecStatusFunc updateExecStatusFunc;

    Map<String, ClusterQueue> CLUSTER_QUEUE_MAP = new ConcurrentHashMap<>();
    Map<String, ClusterListItemResponse> CLUSTER_MAP = new ConcurrentHashMap<>();

    public YarnClusterServiceImpl(PersistenceClient client, UpdateExecStatusFunc updateExecStatusFunc) {
        this.client = client;
        this.updateExecStatusFunc = updateExecStatusFunc;
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
        List<ClusterListItemResponse> clusters = client.getClusterList(request);
        for (ClusterListItemResponse cluster : clusters) {
            CLUSTER_MAP.put(cluster.getName(), cluster);
        }
    }

    /**
     * 定时缓存 集群-队列 资源
     */
    @Scheduled(fixedDelay = 5000)
    public void cacheClusterQueueInfo() {
        logger.info("刷新 YARN 队列资源...");
        Map<String, ClusterQueue> queueMap = new HashMap<>();
        for (ClusterListItemResponse cluster : CLUSTER_MAP.values()) {
            YarnScheduler yarnScheduler = getClusterQueueInfo(cluster.getName(), cluster.getAddress());
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
                continue;
            }

            for (YarnScheduler.Queue queue : queues) {
                String key = ClusterQueue.getClusterQueueKey(cluster.getName(), queue.getQueueName());
                ClusterQueue value = new YarnClusterQueue(queue.getNumPendingApplications());
                queueMap.put(key, value);
            }
        }
        CLUSTER_QUEUE_MAP = queueMap;
    }

    YarnScheduler getClusterQueueInfo(String name, String address) {
        String[] masters = address.split("[,;]");
        for (String master : masters) {
            HttpGet request = new HttpGet(master + "/ws/v1/cluster/scheduler");
            request.addHeader("Content-Type", "application/json");
            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse response = client.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
                    return JsonUtils.parseObject(responseBody, YarnScheduler.class);
                } else {
                    logger.error("获取 YARN 队列信息失败: name: {}, master: {}, code: {}", name, master, statusCode);
                }
            } catch (Throwable t) {
                logger.error("获取 Yarn 队列信息失败: name: {}, master: {}", name, master, t);
            }
        }
        return null;
    }

    /**
     * 刷新引擎执行任务状态
     */
    @Scheduled(fixedDelay = 5000)
    public void refreshExecStatus() {
        for (ClusterListItemResponse cluster : CLUSTER_MAP.values()) {
            refreshExecStatus(cluster);
        }
    }

    /**
     * 刷新引擎执行任务状态, todo: 如何获取各阶段
     * 1小时还没启动可判定失败
     * 半小时内获取最近一小时成功的任务，10分钟获取20分钟内成功的任务，5分钟获取10分钟内成功的任务，1分钟获取2分钟内成功的任务
     * 没启动的任务获取一天内的
     */
    private void refreshExecStatus(ClusterListItemResponse cluster) {
        String[] masters = cluster.getAddress().split("[,;]");
        for (String master : masters) {
            String getYarnAppsUrl = String.format("%s/ws/v1/cluster/apps?applicationTags=%s", master, MapleConstants.TAG_EXEC);
            HttpGet request = new HttpGet(getYarnAppsUrl);
            request.addHeader("Content-Type", "application/json");
            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse response = client.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
                    refresh(responseBody);
                    break;
                } else {
                    logger.error("获取 YARN 作业信息失败: name: {}, master: {}, code: {}", cluster.getName(), master, statusCode);
                }
            } catch (Throwable t) {
                logger.error("获取 YARN 作业信息失败: name: {}, master: {}", cluster.getName(), master, t);
            }
        }
    }

    private void refresh(String responseBody) throws IOException {
        YarnApplications result = JsonUtils.parseObject(responseBody, YarnApplications.class);
        if (result == null || result.getApps() == null || result.getApps().getApp() == null) {
            logger.info("YARN 作业信息为空");
            return;
        }
        for (YarnApplications.Apps.App app : result.getApps().getApp()) {
            String[] tags = app.getApplicationTags().split(",");
            for (String tag : tags) {
                if (!tag.startsWith(MapleConstants.TAG_ID_PREFIX)) {
                    continue;
                }
                String execIdStr = tag.substring(MapleConstants.TAG_ID_PREFIX_LEN);
                if (StringUtils.isBlank(execIdStr) || !StringUtils.isNumeric(execIdStr)) {
                    break;
                }
                Integer execId = Integer.getInteger(execIdStr);
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
                }
                updateExecStatusFunc.apply(execId, state);
            }
        }
    }
}
