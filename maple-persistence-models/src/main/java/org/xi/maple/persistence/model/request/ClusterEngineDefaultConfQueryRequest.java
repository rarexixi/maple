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
public class ClusterEngineDefaultConfQueryRequest extends QueryRequest {

    private Integer id;

    private Collection<Integer> idIn;

    private Integer idMin;

    private Integer idMax;

    private String objType;

    private Collection<String> objTypeIn;

    private String objTypeContains;

    private String objName;

    private Collection<String> objNameIn;

    private String objNameContains;

    private Integer engineId;

    private Collection<Integer> engineIdIn;

    private Integer engineIdMin;

    private Integer engineIdMax;

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

    public void setObjTypeSort(SortConstants sortConstants)  {
        super.orderBy("obj_type", sortConstants);
    }

    public void getObjTypeSort()  {
        super.getOrderBy().getOrDefault("obj_type", null);
    }

    public void setObjNameSort(SortConstants sortConstants)  {
        super.orderBy("obj_name", sortConstants);
    }

    public void getObjNameSort()  {
        super.getOrderBy().getOrDefault("obj_name", null);
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
