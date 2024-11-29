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
public class DatasourceTypeQueryReq extends QueryReq {

    private String typeCode;

    private Collection<String> typeCodeIn;

    private String typeCodeContains;

    private Integer disabled;

    public void setTypeCodeSort(SortConstants sortConstants)  {
        super.orderBy("type_code", sortConstants);
    }

    public void getTypeCodeSort()  {
        super.getOrderBy().getOrDefault("type_code", null);
    }
}
