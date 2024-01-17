package org.xi.maple.builder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.xi.maple.common.model.EngineConf;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class ExecFtlModel<T> {

    public ExecFtlModel(EngineExecutionModel execution) {
        this.execId=execution.getExecId();
        this.execFile=execution.getExecFile();
        this.fromApp=execution.getFromApp();
        this.jobId=execution.getJobId();
        this.bizId=execution.getBizId();
        this.execUniqId=execution.getExecUniqId();
        this.execName=execution.getExecName();
        this.resourceGroup=execution.getResourceGroup();
        this.group=execution.getGroup();
        this.user=execution.getUser();
        this.engine=execution.getEngine();
    }

    private Integer execId;
    private String execFile;
    private String fromApp;
    private String jobId;
    private String bizId;
    private String execUniqId;
    private String execName;
    private String resourceGroup;
    private String group;
    private String user;
    private EngineConf engine;
    private T job;
}
