package org.xi.maple.datacalc.spark.model.sink;

import lombok.Data;
import org.xi.maple.common.model.RdbmsTable;
import org.xi.maple.datacalc.spark.model.SinkConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class JdbcSinkConfig extends SinkConfig {

    @NotBlank
    private String url;

    @NotBlank
    private String driver;

    @NotBlank
    private String user;

    @NotBlank
    private String password;

    @NotNull
    private RdbmsTable targetTable;

    @NotBlank
    @Pattern(regexp = "^(overwrite|append|ignore|error|errorifexists)$", message = "Unknown save mode: {saveMode}. Accepted save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists'.")
    private String saveMode = "overwrite";

    private List<String> preQueries = Collections.emptyList();

    public void setPreQueries(List<String> preQueries) {
        this.preQueries = Optional.ofNullable(preQueries).orElse(this.preQueries);
    }
}
