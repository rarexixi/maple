package org.xi.maple.scheduler.yarn;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.xi.maple.common.constant.ClusterTypeConstants;
import org.xi.maple.scheduler.client.PersistenceClient;
import org.xi.maple.persistence.model.request.ClusterQueryRequest;
import org.xi.maple.persistence.model.response.ClusterListItemResponse;

import java.util.List;

@Component
public class YarnApps {

    private static final Logger logger = LoggerFactory.getLogger(YarnApps.class);

    private static final String TAG_EXEC = "maple-exec";
    private static final String TAG_ID_PREFIX = "maple-id-";

    private final PersistenceClient client;
    private final RestTemplate restTemplate = new RestTemplate();

    public YarnApps(PersistenceClient client) {
        this.client = client;
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
        String getYarnAppsUrl = String.format("%s/ws/v1/cluster/apps?applicationTags=%s", master, TAG_EXEC);

        ResponseEntity<YarnApplications> forEntity = restTemplate.getForEntity(getYarnAppsUrl, YarnApplications.class);
        if (forEntity.getStatusCode() != HttpStatus.OK) {
            return;
        }
        YarnApplications body = forEntity.getBody();
        if (body == null || body.getApps() == null || body.getApps().getApp() == null) {
            return;
        }
        for (YarnApplications.Apps.App app : body.getApps().getApp()) {
            String[] tags = app.applicationTags.split(",");
            for (String tag : tags) {
                if (tag.startsWith(TAG_ID_PREFIX)) {
                    String execId = tag.substring(TAG_ID_PREFIX.length());
                    String state = app.state;
                    String finalStatus = app.finalStatus;
                    // todo 更新状态
                }
            }
        }
    }

    @Data
    public static class YarnApplications {
        private Apps apps;

        @Data
        public static class Apps {
            private List<App> app;

            @Data
            public static class App {
                private String id;
                private String user;
                private String name;
                private String queue;
                private String state;
                private String finalStatus;
                private Double progress;
                private String applicationType;
                private String applicationTags;
                private String logAggregationStatus;
            }
        }
    }
}
