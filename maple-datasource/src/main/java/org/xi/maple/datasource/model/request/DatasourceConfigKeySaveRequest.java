package org.xi.maple.datasource.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.datasource.model.BaseEntity;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class DatasourceConfigKeySaveRequest extends BaseEntity {

    private Integer id;

    private String versions;

    @NotBlank(message = "keyCode(配置编码)不能为空")
    private String keyCode;

    private String keyName;

    private String defaultValue;

    private String valueType;

    private Integer required;

    private String valueRegex;

    private String description;
}
