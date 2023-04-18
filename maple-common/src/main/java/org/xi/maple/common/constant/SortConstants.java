package org.xi.maple.common.constant;

/**
 * @author xishihao
 */
public enum SortConstants {

    ASC(0),
    DESC(1);

    private int code;

    SortConstants(int code) {
        this.code = code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
