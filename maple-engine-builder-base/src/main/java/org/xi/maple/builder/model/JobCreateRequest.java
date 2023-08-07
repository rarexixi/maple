package org.xi.maple.builder.model;

import lombok.Data;

import java.util.Map;

@Data
public class JobCreateRequest {

    private String jobId;

    private String user;

    private String aa;

    private String code;

    private String runType;

    private String engineCategory;

    private String engineVersion;

    private Map<String, ?> engineParameters;

    private Map<String, String> variables;

}
