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

    private Integer owner;

    private Collection<Integer> ownerIn;

    private Integer ownerMin;

    private Integer ownerMax;

    private Integer engineId;

    private Collection<Integer> engineIdIn;

    private Integer engineIdMin;

    private Integer engineIdMax;

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

    public void setOwnerRange(Integer[] ownerRange)  {
        if (ownerRange == null || ownerRange.length != 2) {
            return;
        }
        this.ownerMin = ownerRange[0];
        this.ownerMax = ownerRange[1];
    }

    public void setOwnerSort(SortConstants sortConstants)  {
        super.orderBy("owner", sortConstants);
    }

    public void getOwnerSort()  {
        super.getOrderBy().getOrDefault("owner", null);
    }

    public void setEngineIdRange(Integer[] engineIdRange)  {
        if (engineIdRange == null || engineIdRange.length != 2) {
            return;
        }
        this.engineIdMin = engineIdRange[0];
        this.engineIdMax = engineIdRange[1];
    }

    public void setEngineIdSort(SortConstants sortConstants)  {
        super.orderBy("engine_id", sortConstants);
    }

    public void getEngineIdSort()  {
        super.getOrderBy().getOrDefault("engine_id", null);
    }
}
