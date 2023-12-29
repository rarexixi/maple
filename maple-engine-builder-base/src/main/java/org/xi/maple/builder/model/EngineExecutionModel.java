package org.xi.maple.builder.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class EngineExecutionModel {
    private Integer execId;
    private String execFile;
    private String fromApp;
    private String jobId;
    private String bizId;
    private String execUniqId;
    private String execName;
    private String resourceGroup;
    private String group;
    private String user;
    private String configuration;
}
