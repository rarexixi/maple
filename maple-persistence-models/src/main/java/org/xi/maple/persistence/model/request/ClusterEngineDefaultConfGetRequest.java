package org.xi.maple.persistence.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClusterEngineDefaultConfGetRequest {

    /**
     * 用户组
     */
    private Integer userGroup;

    /**
     * 用户
     */
    private Integer user;
}
