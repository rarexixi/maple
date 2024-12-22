package org.xi.maple.datacalc.spark.model.source;

import lombok.Data;
import org.xi.maple.common.model.RdbmsTable;
import org.xi.maple.datacalc.spark.model.SourceConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class JdbcSourceConfig extends SourceConfig {

    @NotBlank
    private String url;

    @NotBlank
    private String driver;

    @NotBlank
    private String user;

    @NotBlank
    private String password;

    @NotNull
    private RdbmsTable sourceTable;
}
