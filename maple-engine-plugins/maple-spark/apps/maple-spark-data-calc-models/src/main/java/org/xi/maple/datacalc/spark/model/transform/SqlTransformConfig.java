package org.xi.maple.datacalc.spark.model.transform;

import lombok.Data;
import org.xi.maple.datacalc.spark.model.TransformConfig;

import javax.validation.constraints.NotBlank;

@Data
public class SqlTransformConfig extends TransformConfig {

    @NotBlank
    private String sql;
}
