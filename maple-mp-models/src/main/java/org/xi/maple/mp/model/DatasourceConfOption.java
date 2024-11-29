package org.xi.maple.mp.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ToString
public class DatasourceConfOption implements Serializable {

    @NotBlank(message = "keyCode(配置编码)不能为空")
    private String keyCode;

    private String keyName;

    @NotBlank(message = "dataType(数据类型)不能为空")
    private String dataType;

    private String versions;

    private String defaultValue;

    private Boolean nullable;

    private String valueRegex;

    private String description;
}