package org.xi.maple.mp.model.request;

import lombok.Data;
import org.xi.maple.common.constant.SortConstants;
import org.xi.maple.common.model.QueryReq;

import java.io.Serializable;
import java.util.Collection;

@Data
public class JobQueryReq extends QueryReq {

    private Integer id;

    private Collection<Integer> idIn;

    private Integer idMin;

    private Integer idMax;

    private String jobName;

    private Collection<String> jobNameIn;

    private String jobNameContains;

    private String jobType;

    private Collection<String> jobTypeIn;

    private String jobTypeContains;

    private String clusterCategory;

    private Collection<String> clusterCategoryIn;

    private String clusterCategoryContains;

    private String engineCategory;

    private Collection<String> engineCategoryIn;

    private String engineCategoryContains;

    private String engineVersion;

    private Collection<String> engineVersionIn;

    private String engineVersionContains;

    private String owner;

    private Collection<String> ownerIn;

    private String ownerContains;

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

    public void setJobNameSort(SortConstants sortConstants)  {
        super.orderBy("job_name", sortConstants);
    }

    public void getJobNameSort()  {
        super.getOrderBy().getOrDefault("job_name", null);
    }

    public void setJobTypeSort(SortConstants sortConstants)  {
        super.orderBy("job_type", sortConstants);
    }

    public void getJobTypeSort()  {
        super.getOrderBy().getOrDefault("job_type", null);
    }

    public void setClusterCategorySort(SortConstants sortConstants)  {
        super.orderBy("cluster_category", sortConstants);
    }

    public void getClusterCategorySort()  {
        super.getOrderBy().getOrDefault("cluster_category", null);
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

    public void setOwnerSort(SortConstants sortConstants)  {
        super.orderBy("owner", sortConstants);
    }

    public void getOwnerSort()  {
        super.getOrderBy().getOrDefault("owner", null);
    }
}
