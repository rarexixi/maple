package org.xi.maple.common.model.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TableDetailResp {
    List<Map<String, Object>> columns;
    List<Map<String, Object>> pkColumns;
}
