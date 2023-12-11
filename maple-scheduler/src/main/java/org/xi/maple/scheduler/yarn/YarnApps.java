package org.xi.maple.scheduler.yarn;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.xi.maple.common.constant.ClusterTypeConstants;
import org.xi.maple.common.util.JsonUtils;
import org.xi.maple.scheduler.client.PersistenceClient;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;
import org.xi.maple.scheduler.constant.MapleConstants;
import org.xi.maple.scheduler.function.UpdateExecStatusFunc;
import org.xi.maple.scheduler.model.YarnScheduler;
import org.xi.maple.scheduler.yarn.model.YarnApplications;

import java.io.IOException;
import java.util.List;

@Component
public class YarnApps {

    private static final Logger logger = LoggerFactory.getLogger(YarnApps.class);

    private final PersistenceClient client;
    private final UpdateExecStatusFunc updateExecStatusFunc;

    public YarnApps(PersistenceClient client, UpdateExecStatusFunc updateExecStatusFunc) {
        this.client = client;
        this.updateExecStatusFunc = updateExecStatusFunc;
    }

    /**
     * 刷新引擎执行任务状态
     */
    @Scheduled
    public void refreshClusters() {
        ClusterQueryRequest request = new ClusterQueryRequest();
        request.setCategory(ClusterTypeConstants.YARN);
        List<ClusterListItemResponse> clusters = client.getClusterList(request);
        for (ClusterListItemResponse cluster : clusters) {
            String[] masters = cluster.getAddress().split("[,;]");
            for (String master : masters) {
                try {
                    refreshExecStatus(master);
                    break;
                } catch (Throwable t) {
                    logger.error("refresh yarn exec status error", t);
                }
            }
        }
    }

    /**
     * 刷新引擎执行任务状态
     */
    public void refreshExecStatus(String master) {
        String getYarnAppsUrl = String.format("%s/ws/v1/cluster/apps?applicationTags=%s", master, MapleConstants.TAG_EXEC);
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(getYarnAppsUrl);
            request.addHeader("Content-Type", "application/json");
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK) {
                String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");

                YarnApplications result = JsonUtils.parseObject(responseBody, YarnApplications.class);
                for (YarnApplications.Apps.App app : result.getApps().getApp()) {
                    String[] tags = app.getApplicationTags().split(",");
                    for (String tag : tags) {
                        if (tag.startsWith(MapleConstants.TAG_ID_PREFIX)) {
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
            } else {
                logger.error("获取 Yarn 队列信息失败：code:" + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            logger.error("获取 Yarn 队列信息失败", e);
        }
    }


}
