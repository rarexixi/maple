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

    private String clusterId;

    private Collection<String> clusterIdIn;

    private String clusterIdContains;

    private String name;

    private Collection<String> nameIn;

    private String nameContains;

    private String version;

    private Collection<String> versionIn;

    private String versionContains;

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

    public void setClusterIdSort(SortConstants sortConstants)  {
        super.orderBy("cluster_id", sortConstants);
    }

    public void getClusterIdSort()  {
        super.getOrderBy().getOrDefault("cluster_id", null);
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
