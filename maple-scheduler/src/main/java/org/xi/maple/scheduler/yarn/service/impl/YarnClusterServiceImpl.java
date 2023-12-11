package org.xi.maple.scheduler.yarn.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.scheduler.model.MapleClusterQueue;
import org.xi.maple.scheduler.model.YarnCluster;
import org.xi.maple.scheduler.model.YarnScheduler;
import org.xi.maple.scheduler.service.ClusterQueueService;
import org.xi.maple.scheduler.yarn.service.YarnClusterService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YarnClusterServiceImpl implements YarnClusterService {

    private static final Logger logger = LoggerFactory.getLogger(YarnClusterServiceImpl.class);

    final ClusterQueueService clusterQueueService;

    public YarnClusterServiceImpl(ClusterQueueService clusterQueueService) {
        this.clusterQueueService = clusterQueueService;
    }
    /**
     * 定时缓存 集群-队列 资源
     */
    @Scheduled(fixedDelay = 5000)
    public void cacheClusterQueueInfo() {
        logger.info("Reloading cluster queue info...");
        List<YarnCluster> clusters = getClusters();
        Map<String, MapleClusterQueue> queueMap = new HashMap<>();
        /*for (YarnCluster cluster : clusters) {
            String clusterName = cluster.getName();
            YarnScheduler yarnScheduler = getClusterQueueInfo(clusterName);
            if (yarnScheduler == null) {
                continue;
            }
            List<YarnScheduler.Scheduler.SchedulerInfo.Queues.Queue> queues =
                    yarnScheduler.getScheduler().getSchedulerInfo().getQueues().getQueue();

            if (queues == null || queues.isEmpty()) {
                continue;
            }

            for (YarnScheduler.Scheduler.SchedulerInfo.Queues.Queue queue : queues) {
                String key = MapleClusterQueue.getKey(cluster.getName(), queue.getQueueName());
                MapleClusterQueue value = new MapleClusterQueue(queue.getNumPendingApplications());
                queueMap.put(key, value);
            }
        }*/
        queueMap.put(MapleClusterQueue.getKey("hadoop", "default"), new MapleClusterQueue(0));
        clusterQueueService.cacheQueueInfos(queueMap);
    }

    private List<YarnCluster> getClusters() {
        return new ArrayList<>(0);
    }

    YarnScheduler getClusterQueueInfo(String clusterDomain) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(String.format("http://%s/ws/v1/cluster/scheduler", clusterDomain));
            request.addHeader("Content-Type", "application/json");
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
                YarnScheduler result = JsonUtils.parseObject(responseBody, YarnScheduler.class);
                return result;
            } else {
                logger.error("获取 Yarn 队列信息失败：code:" + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            logger.error("获取 Yarn 队列信息失败", e);
        }
        return null;
    }
}
