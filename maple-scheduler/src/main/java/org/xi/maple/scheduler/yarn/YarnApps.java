package org.xi.maple.scheduler.yarn;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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
import org.xi.maple.scheduler.constant.MapleConstants;
import org.xi.maple.scheduler.function.UpdateExecStatusFunc;

import java.util.List;

@Component
public class YarnApps {

    private static final Logger logger = LoggerFactory.getLogger(YarnApps.class);

    private final PersistenceClient client;
    private final UpdateExecStatusFunc updateExecStatusFunc;
    private final RestTemplate restTemplate = new RestTemplate();

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
                if (tag.startsWith(MapleConstants.TAG_ID_PREFIX)) {
                    String execIdStr = tag.substring(MapleConstants.TAG_ID_PREFIX_LEN);
                    if (StringUtils.isBlank(execIdStr) || !StringUtils.isNumeric(execIdStr)) {
                        break;
                    }
                    Integer execId = Integer.getInteger(execIdStr);
                    String state = app.state;
                    String finalStatus = app.finalStatus;
                    updateExecStatusFunc.apply(execId, state);
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
