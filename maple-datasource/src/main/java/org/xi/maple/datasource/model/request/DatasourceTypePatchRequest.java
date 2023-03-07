package org.xi.maple.datasource.model.request;

import org.xi.maple.datasource.model.BaseEntity;


import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DatasourceTypePatchRequest extends BaseEntity {

    private String typeCode;

    private Collection<String> typeCodes;
}
