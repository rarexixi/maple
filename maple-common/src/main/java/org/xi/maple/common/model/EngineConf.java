package org.xi.maple.common.model;

import lombok.Data;

import java.util.Map;

@Data
public class EngineConf {
    private String engineHome;
    private String version;
    private Map<String, ?> engineExtInfo;
    private Map<String, String> envs;
    private Map<String, String> confs;
    private Map<String, String> args;
}
