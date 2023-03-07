package org.xi.maple.common.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateConverter implements Converter<LocalDate> {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";

    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        LocalDateFormat annotation = contentProperty.getField().getAnnotation(LocalDateFormat.class);
        return LocalDate.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(Objects.nonNull(annotation) ? annotation.pattern() : DEFAULT_PATTERN));
    }

    @Override
    public WriteCellData<?> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        LocalDateFormat annotation = contentProperty.getField().getAnnotation(LocalDateFormat.class);
        return new WriteCellData<>(value.format(DateTimeFormatter.ofPattern(Objects.nonNull(annotation) ? annotation.pattern() : DEFAULT_PATTERN)));
    }
}
