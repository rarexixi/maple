package org.xi.maple.common.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateTimeConverter implements Converter<LocalDateTime> {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        LocalDateTimeFormat annotation = contentProperty.getField().getAnnotation(LocalDateTimeFormat.class);
        return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(Objects.nonNull(annotation) ? annotation.pattern() : DEFAULT_PATTERN));
    }

    @Override
    public WriteCellData<?> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        LocalDateTimeFormat annotation = contentProperty.getField().getAnnotation(LocalDateTimeFormat.class);
        return new WriteCellData<>(value.format(DateTimeFormatter.ofPattern(Objects.nonNull(annotation) ? annotation.pattern() : DEFAULT_PATTERN)));
    }
}
