
package org.xi.maple.datacalc.flink.model.definition;

import lombok.Data;

@Data
public class MetadataColumn extends BaseColumn {

    String dataType;
    String metadataKey;
    boolean virtual;
}
