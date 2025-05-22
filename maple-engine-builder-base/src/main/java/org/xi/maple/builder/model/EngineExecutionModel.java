package org.xi.maple.builder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.xi.maple.common.model.EngineConf;
import org.xi.maple.common.util.JsonUtils;

import java.util.Collections;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class EngineExecutionModel {

    /**
     * 操作名称，如 stop，cancel 等
     */
    private String action;

    /**
     * 执行操作所需要的参数
     */
    private Map<String, ?> params;

    private Integer clusterId;
    private String clusterCategory;
    private String engineCategory;
    private String engineVersion;
    private EngineConf engine;

    private Integer execId;
    private String execFile;
    private Integer jobId;
    private String fromApp;
    private String bizId;
    private String execName;
    private String jobType;
    private Map<String, String> resourceGroup;
    private String userGroup;
    private String runBy;

    /**
     * 集群应用ID
     */
    private String clusterAppId;

    /**
     * 集群应用地址
     */
    private String clusterAppAddress;

    private String runConf;
    private String execConf;
    private String execInfo;


    public Map<String, ?> runConf() {
        return JsonUtils.parseObject(this.runConf, Map.class, Collections.emptyMap());
    }

    public Map<String, ?> execConf() {
        return JsonUtils.parseObject(this.execConf, Map.class, Collections.emptyMap());
    }

    public Map<String, ?> execInfo() {
        return JsonUtils.parseObject(this.execInfo, Map.class, Collections.emptyMap());
    }
}







