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
