package org.xi.maple.datasource.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.datasource.model.BaseEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
public class DatasourceTypeSaveRequest extends BaseEntity {

    @NotBlank(message = "typeCode(类型编码)不能为空")
    private String typeCode;

    private String typeName;

    private String icon;

    @NotBlank(message = "classifier(分类)不能为空")
    private String classifier;

    private String versions;

    @Valid
    private List<DatasourceConfigKeySaveRequest> configKeys;
}
