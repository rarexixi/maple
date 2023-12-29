package org.xi.maple.builder.model;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class ExecFtlModel<T> {
    private Integer mapleExecId;
    private String execFile;
    private String fromApp;
    private String jobId;
    private String bizId;
    private String execUniqId;
    private String execName;
    private String resourceGroup;
    private String group;
    private String user;

    private Map<String, String> envs;
    private T job;
}
