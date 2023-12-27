package org.xi.maple.builder.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class EngineExecutionModel {

    private Integer execId;
    private String uniqueId;
    private String execName;
    private String execComment;
    private String execContent;
    private String clusterQueue;
    private String group;
    private String user;
    private String configuration;

}
