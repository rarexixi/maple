package org.xi.maple.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.xi.maple.common.constant.OperateResultType;

/**
 * @author xishihao
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OperateResult<T> {

    private OperateResultType type;
    private T result;

    public static <T> OperateResult<T> newResult(T t) {
        return new OperateResult<>(OperateResultType.NEW, t);
    }

    public static <T> OperateResult<T> updateResult(T t) {
        return new OperateResult<>(OperateResultType.UPDATE, t);
    }
}
