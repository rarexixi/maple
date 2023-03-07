package org.xi.maple.datasource.model.response;

import com.alibaba.excel.annotation.ExcelProperty;
import org.xi.maple.common.excel.*;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.xi.maple.common.utils.JsonUtils;

@Getter
@Setter
@ToString
public class DatasourceListItemResponse implements Serializable {

    @ExcelProperty(value = "Id")
    private Integer id;

    @ExcelProperty(value = "名称")
    private String name;

    @ExcelProperty(value = "描述")
    private String description;

    @ExcelProperty(value = "类型")
    private String datasourceType;

    @ExcelProperty(value = "数据源版本")
    private String version;

    @ExcelProperty(value = "配置JSON")
    private String datasourceConfig;

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

    public Map<String, Object> getDatasourceConfig() {
        return (Map<String, Object>) JsonUtils.parseObject(datasourceConfig, Map.class, new HashMap<String, Object>());
    }
}
