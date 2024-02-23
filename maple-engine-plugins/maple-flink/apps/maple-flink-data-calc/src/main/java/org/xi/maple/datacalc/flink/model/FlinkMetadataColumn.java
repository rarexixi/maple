
package org.xi.maple.datacalc.flink.model;

import lombok.Data;

@Data
public class FlinkMetadataColumn extends FlinkColumn {

    String type;
    String metadataKey;
    boolean virtual;
}
