package org.xi.maple.common.model;

import lombok.Getter;
import org.xi.maple.common.constant.OperateResultType;

/**
 * @author xishihao
 */
@Getter
public class OperateResult<T> {

    private OperateResultType type;
    private T result;

    private OperateResult(OperateResultType type, T result) {
        this.type = type;
        this.result = result;
    }

    public static <T> OperateResult<T> newResult(T t) {
        return new OperateResult<>(OperateResultType.NEW, t);
    }

    public static <T> OperateResult<T> updateResult(T t) {
        return new OperateResult<>(OperateResultType.UPDATE, t);
    }
}
