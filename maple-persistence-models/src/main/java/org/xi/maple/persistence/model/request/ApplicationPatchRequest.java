package org.xi.maple.persistence.model.request;

import org.xi.maple.persistence.model.BaseEntity;
import java.util.Collection;

import lombok.Data;

@Data
public class ApplicationPatchRequest extends BaseEntity {

    private String appName;

    private Collection<String> appNames;
}
