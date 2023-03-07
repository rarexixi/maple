package org.xi.maple.datasource.model.response;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DatasourceTypeDetailResponse extends DatasourceTypeListItemResponse {
    List<DatasourceConfigKeyListItemResponse> configKeys;
}
