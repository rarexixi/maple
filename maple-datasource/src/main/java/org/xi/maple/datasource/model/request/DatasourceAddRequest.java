package org.xi.maple.datasource.model.request;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.common.utils.JsonUtils;
import org.xi.maple.datasource.model.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@ToString
public class DatasourceAddRequest extends BaseEntity {

    private Integer id;

    @NotBlank(message = "name(名称)不能为空")
    private String name;

    private String description;

    @NotBlank(message = "datasourceType(类型)不能为空")
    private String datasourceType;

    @NotBlank(message = "version(数据源版本)不能为空")
    private String version;

    @NotNull(message = "datasourceConfig(配置JSON)不能为空")
    private String datasourceConfig;

    public void setDatasourceConfig(Map<String, String> datasourceConfig) {
        this.datasourceConfig = JsonUtils.toJsonString(datasourceConfig, "{}");
    }
}
