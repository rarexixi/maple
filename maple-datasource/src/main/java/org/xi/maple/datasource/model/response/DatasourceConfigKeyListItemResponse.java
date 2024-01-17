package org.xi.maple.datasource.model.response;

import com.alibaba.excel.annotation.ExcelProperty;


import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.service.excel.LocalDateTimeConverter;
import org.xi.maple.service.excel.LocalDateTimeFormat;

@Getter
@Setter
@ToString
public class DatasourceConfigKeyListItemResponse implements Serializable {

    @ExcelProperty(value = "Id")
    private Integer id;

    @ExcelProperty(value = "类型")
    private String datasourceType;

    @ExcelProperty(value = "版本")
    private String versions;

    @ExcelProperty(value = "配置编码")
    private String keyCode;

    @ExcelProperty(value = "配置名")
    private String keyName;

    @ExcelProperty(value = "配置顺序")
    private Integer keyOrder;

    @ExcelProperty(value = "默认值")
    private String defaultValue;

    @ExcelProperty(value = "类型")
    private String valueType;

    @ExcelProperty(value = "是否必填")
    private Integer required;

    @ExcelProperty(value = "校验正则")
    private String valueRegex;

    @ExcelProperty(value = "配置说明")
    private String description;

    @ExcelProperty(value = "是否删除")
    private Integer deleted;

    @ExcelProperty(value = "创建人")
    private Integer createUser;

    @ExcelProperty(value = "修改人")
    private Integer updateUser;

    @ExcelProperty(value = "创建时间", converter = LocalDateTimeConverter.class)
    @LocalDateTimeFormat
    private LocalDateTime createTime;

    @ExcelProperty(value = "更新时间", converter = LocalDateTimeConverter.class)
    @LocalDateTimeFormat
    private LocalDateTime updateTime;
}
