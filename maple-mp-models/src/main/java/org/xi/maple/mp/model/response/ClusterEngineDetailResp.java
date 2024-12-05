package org.xi.maple.mp.model.response;

import lombok.Data;

@Data
public class ClusterEngineDetailResp extends ClusterEngineItemResp {

    /**
     * 所属集群
     */
    private String clusterText;
}
