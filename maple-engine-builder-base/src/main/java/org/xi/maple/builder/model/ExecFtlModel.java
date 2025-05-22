package org.xi.maple.builder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.xi.maple.common.model.EngineConf;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class ExecFtlModel<R, E> {

    public ExecFtlModel(EngineExecutionModel execution) {
        this.action = execution.getAction();
        this.params = execution.getParams();

        this.clusterId = execution.getClusterId();
        this.clusterCategory = execution.getClusterCategory();
        this.engineCategory = execution.getEngineCategory();
        this.engineVersion = execution.getEngineVersion();
        this.engine = execution.getEngine();

        this.execId = execution.getExecId();
        this.execFile = execution.getExecFile();
        this.jobId = execution.getJobId();
        this.fromApp = execution.getFromApp();
        this.bizId = execution.getBizId();
        this.execName = execution.getExecName();
        this.jobType = execution.getJobType();
        this.userGroup = execution.getUserGroup();
        this.resourceGroup = execution.getResourceGroup();
        this.runBy = execution.getRunBy();

        this.clusterAppId = execution.getClusterAppId();
        this.clusterAppAddress = execution.getClusterAppAddress();

        this.execInfo = execution.execInfo();
    }

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

    private String clusterAppId;
    private String clusterAppAddress;

    private R runConf;
    private E execConf;
    private Map<String, ?> execInfo;
}








