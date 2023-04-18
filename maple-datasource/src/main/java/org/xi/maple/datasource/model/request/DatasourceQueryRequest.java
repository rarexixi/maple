package org.xi.maple.datasource.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryRequest;

import java.util.Collection;

@Getter
@Setter
@ToString
public class DatasourceQueryRequest extends QueryRequest {

    private Integer id;

    private Collection<Integer> idIn;

    private Integer idMin;

    private Integer idMax;

    private String name;

    private String nameContains;

    private String datasourceType;

    private String version;

    private Integer deleted;

    public void setIdRange(Integer[] idRange)  {
        if (idRange == null || idRange.length != 2) {
            return;
        }
        this.idMin = idRange[0];
        this.idMax = idRange[1];
    }

    public void setIdSort(SortConstants sortConstants)  {
        super.orderBy("id", sortConstants);
    }

    public void getIdSort()  {
        super.getOrderBy().getOrDefault("id", null);
    }

    public void setNameSort(SortConstants sortConstants)  {
        super.orderBy("name", sortConstants);
    }

    public void getNameSort()  {
        super.getOrderBy().getOrDefault("name", null);
    }

    public void setDatasourceTypeSort(SortConstants sortConstants)  {
        super.orderBy("datasource_type", sortConstants);
    }

    public void getDatasourceTypeSort()  {
        super.getOrderBy().getOrDefault("datasource_type", null);
    }
}
