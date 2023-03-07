package org.xi.maple.datasource.model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.common.utils.JsonUtils;
import org.xi.maple.datasource.model.BaseEntity;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@ToString
public class DatasourceSaveRequest extends BaseEntity {

    @NotNull(message = "id(Id)不能为空")
    private Integer id;

    private String name;

    private String description;

    private String datasourceType;

    private String version;

    private String datasourceConfig;

    public void setDatasourceConfig(Map<String, String> datasourceConfig) {
        this.datasourceConfig = JsonUtils.toJsonString(datasourceConfig, "{}");
    }
}
