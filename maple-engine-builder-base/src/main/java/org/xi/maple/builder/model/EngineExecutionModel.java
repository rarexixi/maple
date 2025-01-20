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
public class EngineExecutionModel {
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
    private String runConf;
    private String execConf;
}
