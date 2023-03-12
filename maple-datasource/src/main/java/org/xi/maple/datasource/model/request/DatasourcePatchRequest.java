package org.xi.maple.datasource.model.request;

import org.xi.maple.datasource.model.BaseEntity;


import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DatasourcePatchRequest extends BaseEntity {

    private Integer id;

    private Collection<Integer> idIn;
}
