package org.xi.maple.execution.yarn;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class YarnApps {

    public static void main(String[] args) {
        YarnApps yarnApps = new YarnApps();
        yarnApps.updateExecStatus("linkis");
    }

    public void updateExecStatus(String cluster) {
        String master = "http://172.21.212.5:8088";

        String getYarnAppsUrl = String.format("%s/ws/v1/cluster/apps?states=RUNNING&applicationTypes=SPARK", master);

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
                if (tag.startsWith("maple-exec-id-")) {
                    String execId = tag.substring("maple-exec-id-".length());
                    String state = app.state;
                    String finalStatus = app.finalStatus;
                    // todo 更新状态
                }
            }
        }
        System.out.println(body);
    }

    RestTemplate restTemplate = new RestTemplate();


    @Data
    public static class YarnApplications {

        private Apps apps;

        @Data
        public static class Apps {


            private List<App> app;

            @Data
            public static class App {
                private String queue;
                private String state;
                private String finalStatus;
                private String progress;
                private String applicationType;
                private String applicationTags;
                private String logAggregationStatus;
            }
        }
    }
}
