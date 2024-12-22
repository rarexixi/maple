package org.xi.maple.datacalc.flink.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public abstract class MaplePluginConfig implements Serializable {

    @NotBlank
    protected String resultTable;

    protected boolean terminate = false;
}
