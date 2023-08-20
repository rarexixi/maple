package org.xi.maple.common.model;

import lombok.Data;

@Data
public class MapleJsonFormatProperties {

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    private String dateTimeFormat = DATETIME_FORMAT;
    private String dateFormat = DATE_FORMAT;
    private String timeFormat = TIME_FORMAT;


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