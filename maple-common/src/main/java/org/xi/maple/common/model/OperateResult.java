package org.xi.maple.common.model;

import lombok.Getter;

/**
 * @author xishihao
 */
@Getter
public class OperateResult<T> {

    private int type;
    private T result;

    private OperateResult(int type, T result) {
        this.type = type;
        this.result = result;
    }

    public static <T> OperateResult<T> newResult(T t) {
        return new OperateResult<>(Type.NEW, t);
    }

    public static <T> OperateResult<T> updateResult(T t) {
        return new OperateResult<>(Type.UPDATE, t);
    }

    public interface Type {
        int NEW = 1, UPDATE = 0;
    }
}
