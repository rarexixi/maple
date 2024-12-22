package org.xi.maple.datacalc.flink.model.transform;

import lombok.Data;
import org.xi.maple.datacalc.flink.model.TransformConfig;

import javax.validation.constraints.NotBlank;

@Data
public class SqlTransformConfig extends TransformConfig {
    @NotBlank
    String sql;
}
