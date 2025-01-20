package org.xi.maple.common.model;

import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
public class EngineConf {
    private String engineHome;
    private String version;
    private Map<String, String> envs = Collections.emptyMap();
    private Map<String, String> confs = Collections.emptyMap();
}
