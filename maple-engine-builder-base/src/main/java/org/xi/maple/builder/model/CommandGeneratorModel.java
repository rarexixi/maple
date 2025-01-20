package org.xi.maple.builder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandGeneratorModel {

    public CommandGeneratorModel(String filePath, String ftlPath, Object requestModel) {
        this.filePath = filePath;
        this.ftlPath = ftlPath;
        this.requestModel = requestModel;
    }

    String filePath;
    String ftlPath;
    Object requestModel;
    boolean startCommand = false;
}
