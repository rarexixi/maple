package org.xi.maple.mp.model.request;

import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryReq;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import lombok.Data;

@Data
public class DatasourceQueryReq extends QueryReq {

    private Integer id;

    private Collection<Integer> idIn;

    private Integer idMin;

    private Integer idMax;

    private String name;

    private Collection<String> nameIn;

    private String nameContains;

    private String datasourceType;

    private Collection<String> datasourceTypeIn;

    private String datasourceTypeContains;

    private Integer disabled;

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
