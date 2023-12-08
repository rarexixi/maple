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
public class ApplicationQueryRequest extends QueryRequest {

    private String appName;

    private Collection<String> appNameIn;

    private String appNameContains;

    private Integer deleted;

    public void setAppNameSort(SortConstants sortConstants)  {
        super.orderBy("app_name", sortConstants);
    }

    public void getAppNameSort()  {
        super.getOrderBy().getOrDefault("app_name", null);
    }
}
