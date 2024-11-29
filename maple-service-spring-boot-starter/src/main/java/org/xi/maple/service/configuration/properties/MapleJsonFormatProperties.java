package org.xi.maple.service.configuration.properties;

import lombok.Data;
import org.xi.maple.common.util.JsonUtils;

@Data
public class MapleJsonFormatProperties {

    private String dateTimeFormat = JsonUtils.DATETIME_FORMAT;
    private String dateFormat = JsonUtils.DATE_FORMAT;
    private String timeFormat = JsonUtils.TIME_FORMAT;


    public void setDateTimeFormat(String dateTimeFormat) {
        if (dateTimeFormat != null) {
            this.dateTimeFormat = dateTimeFormat;
        }
    }

    public void setDateFormat(String dateFormat) {
        if (dateFormat != null) {
            this.dateFormat = dateFormat;
        }
    }

    public void setTimeFormat(String timeFormat) {
        if (timeFormat != null) {
            this.timeFormat = timeFormat;
        }
    }
}