package org.xi.maple.common.model.request;

import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
public class GetDatabasesReq extends JdbcBaseReq {

    @Override
    public Map<String, ?> getSqlParamMap() {
        return Collections.emptyMap();
    }
}
