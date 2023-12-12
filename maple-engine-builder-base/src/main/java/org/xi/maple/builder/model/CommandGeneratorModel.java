package org.xi.maple.builder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandGeneratorModel {

    public CommandGeneratorModel(String ftlPath, String filePath, Object requestModel) {
        this.ftlPath = ftlPath;
        this.filePath = filePath;
        this.requestModel = requestModel;
    }

    boolean startCommand = false;
    String ftlPath;
    String filePath;
    Object requestModel;
}
