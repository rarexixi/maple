package org.xi.maple.persistence.model.request;

import org.xi.maple.persistence.model.BaseEntity;
import java.util.Collection;

import lombok.Data;

@Data
public class ClusterEngineDefaultConfPatchRequest extends BaseEntity {

    private Integer id;

    private Collection<Integer> ids;
}
