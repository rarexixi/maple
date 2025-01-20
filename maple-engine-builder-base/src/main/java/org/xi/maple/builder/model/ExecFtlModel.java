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
public class ExecFtlModel<R, E> {

    public ExecFtlModel(EngineExecutionModel execution) {
        this.execId=execution.getExecId();
        this.execFile=execution.getExecFile();
        this.fromApp=execution.getFromApp();
        this.jobId=execution.getJobId();
        this.jobType=execution.getJobType();
        this.bizId=execution.getBizId();
        this.execName=execution.getExecName();
        this.userGroup=execution.getUserGroup();
        this.runBy=execution.getRunBy();
        this.engine=execution.getEngine();
    }

    private Integer execId;
    private String execFile;
    private String fromApp;
    private Integer jobId;
    private String jobType;
    private String bizId;
    private String execName;
    private String userGroup;
    private String runBy;
    private EngineConf engine;
    private R runConf;
    private E execConf;
}
