package org.xi.maple.common.annotation;

import org.xi.maple.common.constant.SetFieldType;

import java.lang.annotation.*;

/**
 * @author xishihao
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SetField {
    /**
     * 要设置的字段
     */
    String field() default "";

    /**
     * 设置的条件
     */
    SetFieldType[] types() default {};

    /**
     * 默认值
     */
    String defaultValue() default "";

    /**
     * 是否强制设置
     */
    boolean force() default true;
}
