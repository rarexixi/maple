package org.xi.maple.common.constant;

public enum MapleErrors {

    // region 参数错误

    // region 参数错误

    PARAM_ERROR(100, "param error"),
    PARAM_NULL(101, "param null"),
    PARAM_INVALID(102, "param invalid"),
    PARAM_NOT_MATCH(103, "param not match"),
    PARAM_NOT_VALID(104, "param not valid"),

    // endregion

    // endregion

    // region 数据错误
    DATA_EXIST(1000, "data exist"),
    DATA_NOT_EXIST(1001, "data not exist"),
    DATA_INSERT_ERROR(1002, "data insert error"),
    DATA_UPDATE_ERROR(1003, "data update error"),
    DATA_DELETE_ERROR(1004, "data delete error"),
    // endregion

    // region 系统错误
    SYSTEM_ERROR(2000, "system error"),
    SYSTEM_BUSY(2001, "system busy"),
    SYSTEM_TIMEOUT(2002, "system timeout"),
    SYSTEM_CONFIG_ERROR(2003, "system config error"),
    SYSTEM_NOT_SUPPORT(2004, "system not support"),
    SYSTEM_NOT_LOGIN(2005, "system not login"),
    SYSTEM_NOT_AUTH(2006, "system not auth"),
    SYSTEM_NOT_FOUND(2007, "system not found"),
    SYSTEM_NOT_ALLOW(2008, "system not allow"),
    SYSTEM_NOT_ACCESS(2009, "system not access"),
    SYSTEM_NOT_CONNECT(2010, "system not connect"),
    SYSTEM_NOT_AVAILABLE(2011, "system not available"),
    SYSTEM_NOT_IMPLEMENT(2012, "system not implement"),
    SYSTEM_NOT_MATCH(2013, "system not match"),
    SYSTEM_NOT_VALID(2014, "system not valid"),
    SYSTEM_NOT_READY(2015, "system not ready"),
    SYSTEM_NOT_EXIST(2016, "system not exist"),
    SYSTEM_NOT_EMPTY(2017, "system not empty"),
    SYSTEM_NOT_UNIQUE(2018, "system not unique"),
    SYSTEM_NOT_SUPPORT_OPERATION(2019, "system not support operation"),
    SYSTEM_NOT_SUPPORT_DATA_TYPE(2020, "system not support data type"),
    SYSTEM_NOT_SUPPORT_DATA_SOURCE(2021, "system not support data source"),

    SYSTEM_UNKNOWN_ERROR(2999, "system unknown error"),
    // endregion


    SUCCESS(0, "success"),
    UNKNOWN_ERROR(1, "unknown error");

    final int code;
    final String msg;

    MapleErrors(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
