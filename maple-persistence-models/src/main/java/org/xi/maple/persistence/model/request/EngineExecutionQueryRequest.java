package org.xi.maple.persistence.model.request;

import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryRequest;

import java.util.Collection;

import lombok.Data;

@Data
public class EngineExecutionQueryRequest extends QueryRequest {

    private Integer id;

    private Collection<Integer> idIn;

    private Integer idMin;

    private Integer idMax;

    private String uniqueId;

    private Collection<String> uniqueIdIn;

    private String uniqueIdContains;

    private String execName;

    private Collection<String> execNameIn;

    private String execNameContains;

    private String fromApp;

    private Collection<String> fromAppIn;

    private String fromAppContains;

    private String cluster;

    private Collection<String> clusterIn;

    private String clusterContains;

    private String clusterQueue;

    private Collection<String> clusterQueueIn;

    private String clusterQueueContains;

    private String engineCategory;

    private Collection<String> engineCategoryIn;

    private String engineCategoryContains;

    private String engineVersion;

    private Collection<String> engineVersionIn;

    private String engineVersionContains;

    private String status;

    private Collection<String> statusIn;

    private String statusContains;

    private String group;

    private Collection<String> groupIn;

    private String groupContains;

    private String user;

    private Collection<String> userIn;

    private String userContains;

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

    public void setUniqueIdSort(SortConstants sortConstants)  {
        super.orderBy("unique_id", sortConstants);
    }

    public void getUniqueIdSort()  {
        super.getOrderBy().getOrDefault("unique_id", null);
    }

    public void setExecNameSort(SortConstants sortConstants)  {
        super.orderBy("exec_name", sortConstants);
    }

    public void getExecNameSort()  {
        super.getOrderBy().getOrDefault("exec_name", null);
    }

    public void setFromAppSort(SortConstants sortConstants)  {
        super.orderBy("from_app", sortConstants);
    }

    public void getFromAppSort()  {
        super.getOrderBy().getOrDefault("from_app", null);
    }

    public void setClusterSort(SortConstants sortConstants)  {
        super.orderBy("cluster", sortConstants);
    }

    public void getClusterSort()  {
        super.getOrderBy().getOrDefault("cluster", null);
    }

    public void setClusterQueueSort(SortConstants sortConstants)  {
        super.orderBy("cluster_queue", sortConstants);
    }

    public void getClusterQueueSort()  {
        super.getOrderBy().getOrDefault("cluster_queue", null);
    }

    public void setEngineCategorySort(SortConstants sortConstants)  {
        super.orderBy("engine_category", sortConstants);
    }

    public void getEngineCategorySort()  {
        super.getOrderBy().getOrDefault("engine_category", null);
    }

    public void setEngineVersionSort(SortConstants sortConstants)  {
        super.orderBy("engine_version", sortConstants);
    }

    public void getEngineVersionSort()  {
        super.getOrderBy().getOrDefault("engine_version", null);
    }

    public void setStatusSort(SortConstants sortConstants)  {
        super.orderBy("status", sortConstants);
    }

    public void getStatusSort()  {
        super.getOrderBy().getOrDefault("status", null);
    }

    public void setGroupSort(SortConstants sortConstants)  {
        super.orderBy("group", sortConstants);
    }

    public void getGroupSort()  {
        super.getOrderBy().getOrDefault("group", null);
    }

    public void setUserSort(SortConstants sortConstants)  {
        super.orderBy("user", sortConstants);
    }

    public void getUserSort()  {
        super.getOrderBy().getOrDefault("user", null);
    }
}
