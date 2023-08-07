package org.xi.maple.builder.model;

import lombok.Data;

@Data
public class CommandGeneratorModel {
    boolean startCommand;
    String ftlPath;
    String filePath;
    Object requestModel;
}
