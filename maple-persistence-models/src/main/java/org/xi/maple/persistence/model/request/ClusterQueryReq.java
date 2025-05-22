package org.xi.maple.persistence.model.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.xi.maple.common.model.QueryReq;


import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClusterQueryReq extends QueryReq {

    private String category;
}
