package org.xi.maple.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xishihao
 */
public class ArrayUtils {

    private static final Logger logger = LoggerFactory.getLogger(ArrayUtils.class);


    /**
     * 判断数组是否包含某个值
     * @param array 数组
     * @param value 值
     * @return 是否包含
     * @param <T> 类型
     */
    public static <T> boolean contains(T[] array, T value) {
        if (array == null || array.length == 0 || value == null) {
            return false;
        }
        for (T t : array) {
            if (t.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
