package org.xi.maple.persistence.model.request;

import lombok.Data;
import org.xi.maple.common.model.QueryRequest;

import java.util.Collection;

@Data
public class ClusterPatchRequest extends QueryRequest {

    private String name;

    private Collection<String> names;
}
