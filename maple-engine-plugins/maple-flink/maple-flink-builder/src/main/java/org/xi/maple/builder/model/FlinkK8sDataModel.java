package org.xi.maple.builder.model;

import lombok.Data;

import java.util.Map;

@Data
public class FlinkK8sDataModel {
    private String namespace;

    private String queue;

    private Boolean jobManagerHaEnable;
    private Integer jobManagerReplicas;
    private Integer jobManagerCores;
    private String jobManagerMemory;

    private Integer parallelism;
    private Integer taskManagerCores;
    private String taskManagerMemory;
    private Integer numberOfTaskSlots;

    private Map<String, String> conf;

    private String runType;

    private Map<String, Object> runConf;
}
