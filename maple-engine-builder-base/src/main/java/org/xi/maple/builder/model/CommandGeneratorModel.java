package org.xi.maple.builder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandGeneratorModel {
    boolean startCommand;
    String ftlPath;
    String filePath;
    Object requestModel;
}
