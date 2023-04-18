package org.xi.maple.datasource.model.response;

import com.alibaba.excel.annotation.ExcelProperty;


import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.common.excel.LocalDateTimeConverter;
import org.xi.maple.common.excel.LocalDateTimeFormat;

@Getter
@Setter
@ToString
public class DatasourceTypeListItemResponse implements Serializable {

    @ExcelProperty(value = "类型编码")
    private String typeCode;

    @ExcelProperty(value = "类型名称")
    private String typeName;

    @ExcelProperty(value = "图标地址")
    private String icon;

    @ExcelProperty(value = "分类")
    private String classifier;

    @ExcelProperty(value = "版本")
    private String versions;

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
