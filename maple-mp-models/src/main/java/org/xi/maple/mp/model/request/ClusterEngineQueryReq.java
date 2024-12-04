package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryReq;

import java.io.Serializable;
import java.util.Collection;

@Data
public class ClusterEngineQueryReq extends QueryReq {

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
