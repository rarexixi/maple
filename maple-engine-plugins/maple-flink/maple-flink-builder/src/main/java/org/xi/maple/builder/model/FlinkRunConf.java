package org.xi.maple.builder.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

public class FlinkRunConf {

    @Data
    public static abstract class RunConf implements Serializable {
        private String jobManagerMemory;

        private Integer parallelism;
        private Integer taskManagerCores;
        private String taskManagerMemory;
        private Integer numberOfTaskSlots;

        private Map<String, String> conf;

        private Map<String, Object> runConf;
    }

    @Data
    public static class K8s extends RunConf {
        private String namespace;

        private String queue;

        private Boolean jobManagerHaEnable;
        private Integer jobManagerReplicas;
        private Integer jobManagerCores;
    }

    @Data
    public static class Yarn extends RunConf {
        private String queue;
    }
}
