package org.xi.maple.common.constant;

public enum MapleErrors {

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
