package org.xi.maple.builder.model;

import lombok.Data;

import java.util.Map;

@Data
public class FlinkYarnDataModel {

    private String queue;

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
