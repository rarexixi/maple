package org.xi.maple.datasource.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryRequest;

@Getter
@Setter
@ToString
public class DatasourceTypeQueryRequest extends QueryRequest {

    private String typeCode;

    private String typeCodeContains;

    private Integer deleted;

    public void setTypeCodeSort(SortConstants sortConstants)  {
        super.orderBy("type_code", sortConstants);
    }

    public void getTypeCodeSort()  {
        super.getOrderBy().getOrDefault("type_code", null);
    }
}
