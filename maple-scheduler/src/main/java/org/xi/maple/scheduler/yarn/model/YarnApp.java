package org.xi.maple.scheduler.yarn.model;

import lombok.Data;

@Data
public class YarnApp {
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