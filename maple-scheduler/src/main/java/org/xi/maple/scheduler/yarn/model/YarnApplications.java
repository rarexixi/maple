package org.xi.maple.scheduler.yarn.model;

import lombok.Data;

import java.util.List;

@Data
public class YarnApplications {
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