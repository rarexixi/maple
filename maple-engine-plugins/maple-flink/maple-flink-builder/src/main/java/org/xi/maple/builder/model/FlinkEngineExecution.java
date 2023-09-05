package org.xi.maple.builder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class FlinkEngineExecution {

    private Map<String, String> envs;

    private String runParameters;
}
