package org.xi.maple.persistence.model.request;

import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import lombok.Data;

@Data
public class ClusterEngineQueryRequest extends QueryRequest {

    private Integer id;

    private Collection<Integer> idIn;

    private Integer idMin;

    private Integer idMax;

    private String cluster;

    private Collection<String> clusterIn;

    private String clusterContains;

    private String name;

    private Collection<String> nameIn;

    private String nameContains;

    private String version;

    private Collection<String> versionIn;

    private String versionContains;

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

    public void setClusterSort(SortConstants sortConstants)  {
        super.orderBy("cluster", sortConstants);
    }

    public void getClusterSort()  {
        super.getOrderBy().getOrDefault("cluster", null);
    }

    public void setNameSort(SortConstants sortConstants)  {
        super.orderBy("name", sortConstants);
    }

    public void getNameSort()  {
        super.getOrderBy().getOrDefault("name", null);
    }

    public void setVersionSort(SortConstants sortConstants)  {
        super.orderBy("version", sortConstants);
    }

    public void getVersionSort()  {
        super.getOrderBy().getOrDefault("version", null);
    }
}
